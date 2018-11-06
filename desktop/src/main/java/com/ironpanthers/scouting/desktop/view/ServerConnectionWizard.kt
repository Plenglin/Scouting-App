package com.ironpanthers.scouting.desktop.view

import com.intel.bluetooth.MicroeditionConnector
import com.ironpanthers.scouting.BLUETOOTH_NAME
import com.ironpanthers.scouting.BLUETOOTH_SERVER_UUID_RAW
import javafx.scene.Parent
import org.apache.log4j.BasicConfigurator
import org.slf4j.LoggerFactory
import tornadofx.View
import tornadofx.listview
import javax.microedition.io.Connector
import javax.microedition.io.StreamConnectionNotifier

class ServerConnectionWizard : View() {
    override val root: Parent

    init {
        root = listview<Any> {

        }
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
