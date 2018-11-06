package com.ironpanthers.scouting.desktop.view

import com.ironpanthers.scouting.BLUETOOTH_SERVER_UUID_RAW
import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.FXCollections
import javafx.scene.Parent
import javafx.scene.control.SelectionMode
import org.slf4j.LoggerFactory
import tornadofx.*
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import javax.bluetooth.*
import javax.bluetooth.UUID

class ServerConnectionWizard : View(), DiscoveryListener {
    override val root: Parent
    private val logger = LoggerFactory.getLogger(javaClass)

    private val services = FXCollections.observableArrayList<ServiceRecord>()
    var result: ServiceRecord? = null

    private val isSearchingProperty = SimpleBooleanProperty(false)
    private var isSearching by isSearchingProperty

    init {
        title = "Select a server..."
        root = vbox {
            toolbar {
                button("Refresh") {
                    disableProperty().bind(isSearchingProperty)
                    action {
                        beginInquiry()
                    }
                }
            }
            listview<ServiceListing> {
                items.bind(services) {
                    ServiceListing(it)
                }
                selectionModel.selectionMode = SelectionMode.SINGLE
                onUserSelect {
                    logger.info("User selected $it")
                    result = it.serviceRecord
                    modalStage?.close()
                }
            }
        }

        beginInquiry()
    }

    private fun beginInquiry() {
        logger.debug("Beginning inquiry")
        if (!isSearching) {
            isSearching = true
            LocalDevice.getLocalDevice().discoveryAgent.startInquiry(DiscoveryAgent.GIAC, this)
        }
    }

    override fun serviceSearchCompleted(transID: Int, respCode: Int) {
        logger.debug("Finished service search, attempting to search next device")
        trySearchNextDevice()
    }

    private val devices = ConcurrentLinkedQueue<RemoteDevice>()
    private var currentDevice: RemoteDevice? = null

    override fun deviceDiscovered(btDevice: RemoteDevice, cod: DeviceClass) {
        logger.debug("Discovered $btDevice of class $cod")
        devices.add(btDevice)
        //services.add(btDevice)
    }

    override fun servicesDiscovered(transID: Int, servRecord: Array<out ServiceRecord>) {
        logger.debug("Discovered services: {}", Arrays.toString(servRecord))
        services.addAll(servRecord)
    }

    override fun inquiryCompleted(discType: Int) {
        logger.debug("Inquiry complete $discType")
        trySearchNextDevice()
    }

    private fun trySearchNextDevice() {
        if (devices.isNotEmpty()) {
            searchNextDevice()
        } else {
            isSearching = false
        }
    }

    private fun searchNextDevice() {
        val dev = devices.remove()
        currentDevice = dev
        logger.debug("Querying {} for services", dev)
        LocalDevice.getLocalDevice().discoveryAgent.searchServices(
                intArrayOf(),
                arrayOf(UUID(BLUETOOTH_SERVER_UUID_RAW, false)),
                dev,
                this
        )
    }

}

private data class ServiceListing(val serviceRecord: ServiceRecord) {
    override fun toString(): String {
        return "${serviceRecord.hostDevice}"
    }
}