package com.ironpanthers.scouting.desktop.view.connection

import com.ironpanthers.scouting.desktop.io.BluetoothPeer
import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.FXCollections
import javafx.scene.Parent
import javafx.scene.control.SelectionMode
import org.slf4j.LoggerFactory
import tornadofx.*
import javax.bluetooth.*

class PeerDiscoveryDialog : View(), DiscoveryListener {
    override val root: Parent
    private val logger = LoggerFactory.getLogger(javaClass)

    private val devices = FXCollections.observableArrayList<RemoteDevice>()
    private var result: RemoteDevice? = null

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
            listview<DeviceListing> {
                items.bind(devices) {
                    DeviceListing(it)
                }
                selectionModel.selectionMode = SelectionMode.SINGLE
                onUserSelect {
                    logger.info("User selected $it")
                    result = it.dev
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

    fun consumeResult(): BluetoothPeer? {
        val out = result
        result = null
        if (out == null) {
            return null
        }
        return BluetoothPeer(out)
    }

    override fun deviceDiscovered(btDevice: RemoteDevice, cod: DeviceClass) {
        logger.debug("Discovered {} of class {}", btDevice, cod)
        devices.add(btDevice)
    }

    override fun inquiryCompleted(discType: Int) {
        logger.debug("Inquiry complete {}", discType)
        //trySearchNextDevice()
    }

    override fun servicesDiscovered(transID: Int, servRecord: Array<out ServiceRecord>) {
        //logger.debug("Discovered devices: {}", Arrays.toString(servRecord))
        //services.addAll(servRecord)
        //services.add()
    }

    override fun serviceSearchCompleted(transID: Int, respCode: Int) {
        //logger.debug("Finished service search, attempting to search next device")
        //trySearchNextDevice()
    }

}

private data class DeviceListing(val dev: RemoteDevice) {
    override fun toString(): String {
        return "${dev.getFriendlyName(true)} (${dev.bluetoothAddress})"
    }
}