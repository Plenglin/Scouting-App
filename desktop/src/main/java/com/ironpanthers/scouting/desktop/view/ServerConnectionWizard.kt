package com.ironpanthers.scouting.desktop.view

import com.intel.bluetooth.MicroeditionConnector
import com.ironpanthers.scouting.BLUETOOTH_NAME
import com.ironpanthers.scouting.BLUETOOTH_SERVER_UUID_RAW
import javafx.collections.FXCollections
import javafx.scene.Parent
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.control.SelectionMode
import javafx.util.Callback
import org.apache.log4j.BasicConfigurator
import org.slf4j.LoggerFactory
import tornadofx.*
import javax.bluetooth.*
import javax.microedition.io.Connector
import javax.microedition.io.StreamConnectionNotifier

class ServerConnectionWizard : View(), DiscoveryListener {
    override val root: Parent
    private val logger = LoggerFactory.getLogger(javaClass)

    private val clients = FXCollections.observableArrayList<RemoteDevice>()
    var result: RemoteDevice? = null

    init {
        LocalDevice.getLocalDevice().discoveryAgent.startInquiry(DiscoveryAgent.GIAC, this)
        title = "Select a server..."
        root = vbox {
            label("Servers")
            listview<RemoteDevice> {
                items.bind(clients) { it }
                setCellFactory {
                    object : ListCell<RemoteDevice>() {
                        override fun updateItem(item: RemoteDevice?, empty: Boolean) {
                            super.updateItem(item, empty)
                            text = (if (empty) "" else item!!.bluetoothAddress)
                        }
                    }
                }
                selectionModel.selectionMode = SelectionMode.SINGLE
                onUserSelect {
                    logger.info("User selected $it")
                    result = it
                    modalStage?.close()
                }
            }
        }
    }

    override fun serviceSearchCompleted(transID: Int, respCode: Int) {
    }

    override fun deviceDiscovered(btDevice: RemoteDevice, cod: DeviceClass) {
        logger.debug("Discovered $btDevice of class $cod")
        clients.add(btDevice)
    }

    override fun servicesDiscovered(transID: Int, servRecord: Array<out ServiceRecord>?) {
    }

    override fun inquiryCompleted(discType: Int) {
        logger.debug("Inquiry complete $discType")
    }

}

fun main(args: Array<String>) {
    BasicConfigurator.configure()
    val logger = LoggerFactory.getLogger("test")
    /*LocalDevice.getLocalDevice().discoveryAgent.startInquiry(DiscoveryAgent.GIAC, object : DiscoveryListener {
        override fun serviceSearchCompleted(transID: Int, respCode: Int) {
        }

        override fun deviceDiscovered(btDevice: RemoteDevice?, cod: DeviceClass?) {
            logger.info("Discovered $btDevice of class $cod")
        }

        override fun servicesDiscovered(transID: Int, servRecord: Array<out ServiceRecord>?) {
        }

        override fun inquiryCompleted(discType: Int) {
            logger.info("Inquiry complete $discType")
        }

    })*/

    val url = "btspp://localhost:$BLUETOOTH_SERVER_UUID_RAW;name=$BLUETOOTH_NAME"
    val c = MicroeditionConnector.open(url, Connector.READ_WRITE, false) as StreamConnectionNotifier
    logger.info("waiting on $url")
    val sc = c.acceptAndOpen()
    Thread.sleep(10000L)
}
