package com.ironpanthers.scouting.desktop.view.connection

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.intel.bluetooth.MicroeditionConnector
import com.ironpanthers.scouting.BLUETOOTH_MAIN_UUID_RAW
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.Parent
import javafx.scene.control.TreeItem
import org.controlsfx.control.ToggleSwitch
import org.slf4j.LoggerFactory
import tornadofx.*
import javax.bluetooth.RemoteDevice
import javax.microedition.io.StreamConnectionNotifier

typealias UserClickListener = () -> Unit

class ConnectionView : View() {

    override val root: Parent
    val serverEnabledProperty = SimpleBooleanProperty()
    private val logger = LoggerFactory.getLogger(javaClass)
    //private val server = BluetoothServer()
    private val mapper = jacksonObjectMapper()
    private val peers = observableList<RemoteDevice>()

    init {
        root = vbox {
            toolbar {
                add(ToggleSwitch("Master").apply {
                    serverEnabledProperty.bind(selectedProperty())
                })
                button("+") {
                    action {
                        val wizard = find<PeerDiscoveryDialog>()
                        wizard.openModal(block = true)
                        val dev = wizard.result
                        logger.info("Got device {}", dev)
                        if (dev != null) {
                            peers.add(dev)
                        }
                        //logger.debug("Connecting to URL {}", wizard.result?.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false))
                    }
                }
                button("Refresh")
            }
            val rootTreeItem = TreeItem<DeviceNodeData>()
            rootTreeItem.isExpanded = true
            rootTreeItem.children.bind(peers) { dev ->
                TreeItem(DeviceNodeData(dev, NodeType.ROOT)).apply {
                    treeitem(DeviceNodeData(dev, NodeType.CONNECT_CHAT))
                    treeitem(DeviceNodeData(dev, NodeType.CONNECT_MASTER))
                }
            }
            treetableview(rootTreeItem) {
                isShowRoot = false
                column<DeviceNodeData, String>("Name") {
                    val nodeData = it.value.value
                    nodeData.nodeTitleProperty
                }
                onUserSelect(2) {
                    logger.info("User selected node {}", it)
                    when (it.type) {
                        NodeType.CONNECT_MASTER -> {
                            logger.debug("Connecting to {}", it.url)
                            val notif = MicroeditionConnector.open(it.url) as StreamConnectionNotifier
                            val conn = notif.acceptAndOpen()
                            logger.debug("conn: {}", conn)
                        }
                        NodeType.CONNECT_CHAT -> TODO()
                        NodeType.ROOT -> TODO()
                    }
                }
            }
        }

        serverEnabledProperty.onChange {
            if (it) {
                startServer()
            } else {
                stopServer()
            }
        }

        //server.endpoint("robot-performance") {
            //mapper.treeToValue()
        //}
    }

    private fun findServices() {

    }

    private fun startServer() {
        //server.start()
    }

    private fun stopServer() {
        //server.close()
    }
}

data class DeviceNodeWrapper(val dev: RemoteDevice) {
    val nameProperty = SimpleStringProperty(dev.bluetoothAddress)
}

data class DeviceNodeData(val dev: RemoteDevice, val type: NodeType) {
    val url = "btspp://${dev.bluetoothAddress}:$BLUETOOTH_MAIN_UUID_RAW;authenticate=false;encrypt=false;master=false"
    val nameProperty = SimpleStringProperty(dev.bluetoothAddress)
    val isConnectedProperty = SimpleBooleanProperty(false)

    val nodeTitleProperty by lazy {
        SimpleStringProperty(when (type) {
            NodeType.ROOT -> dev.bluetoothAddress
            NodeType.CONNECT_CHAT -> "Connect Chat Relay"
            NodeType.CONNECT_MASTER -> "Connect Match Manager"
        })
    }

}

enum class NodeType {
    ROOT, CONNECT_CHAT, CONNECT_MASTER
}