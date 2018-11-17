package com.ironpanthers.scouting.desktop.view.connection

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.intel.bluetooth.MicroeditionConnector
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.collections.FXCollections
import javafx.scene.Parent
import javafx.scene.control.TreeItem
import org.controlsfx.control.ToggleSwitch
import org.slf4j.LoggerFactory
import tornadofx.*
import javax.bluetooth.RemoteDevice
import javax.bluetooth.ServiceRecord
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

            val rootTreeItem = TreeItem<ConnectionTreeNode>(Root())
            rootTreeItem.isExpanded = true
            rootTreeItem.children.bind(peers) { dev: RemoteDevice ->
                val wrapper = Device(dev)
                val tree = TreeItem<ConnectionTreeNode>(wrapper)
                tree.children.bind(wrapper.children) { TreeItem(it) }
                tree
            }

            treeview(rootTreeItem) {
                isShowRoot = false
                /*column<ConnectionTreeNode, String>("Name") {
                    val nodeData = it.value.value
                    nodeData.nameProperty
                }*/
                /*onUserSelect(2) {
                    logger.info("User selected node {}", it)
                    when (it) {
                        is MatchMaster -> {
                            val service = it.serviceRecord
                            val url = service.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false)

                            logger.info("Connecting to {}", url)
                            val notif = MicroeditionConnector.open(url) as StreamConnectionNotifier
                            val conn = notif.acceptAndOpen()
                            logger.debug("conn: {}", conn)
                        }
                        is Chat -> {

                        }
                    }
                }*/
                onUserSelect {
                    logger.info("User selected node {}", it)
                    when (it) {
                        is MatchMaster -> {
                            val service = it.serviceRecord
                            val url = service.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false)

                            logger.info("Connecting to {}", url)
                            val notif = MicroeditionConnector.open(url) as StreamConnectionNotifier
                            val conn = notif.acceptAndOpen()
                            logger.debug("conn: {}", conn)
                        }
                        is Chat -> {

                        }
                    }
                }
            }
        }

        serverEnabledProperty.onChange {
            logger.debug("Setting server enabled to {}")
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

sealed class ConnectionTreeNode {
    abstract val nameProperty: StringProperty
}

private class Root : ConnectionTreeNode() {
    override val nameProperty: StringProperty = SimpleStringProperty()
}

private class Device(dev: RemoteDevice) : ConnectionTreeNode() {
    override val nameProperty: StringProperty = SimpleStringProperty(dev.getFriendlyName(true))
    val isSearchingProperty = SimpleBooleanProperty(false)

    var foundMatch: ServiceRecord? = null
    var foundChat: ServiceRecord? = null

    val children = FXCollections.observableArrayList<ConnectionTreeNode>()

    init {
        isSearchingProperty.onChange { searching ->
            if (searching) {
                children.setAll(Loading())
            } else {
                foundChat?.let {
                    children.add(Chat(it))
                }
                foundMatch?.let {
                    children.add(MatchMaster(it))
                }
            }
        }
    }
}

private class Loading : ConnectionTreeNode() {
    override val nameProperty: StringProperty = SimpleStringProperty("Loading...")
}

private class Chat(val serviceRecord: ServiceRecord) : ConnectionTreeNode() {
    val connectedProperty = SimpleBooleanProperty(false)
    override val nameProperty: StringProperty = SimpleStringProperty("Chat Relay")
}

private class MatchMaster(val serviceRecord: ServiceRecord) : ConnectionTreeNode() {
    val connectedProperty = SimpleBooleanProperty(false)
    override val nameProperty: StringProperty = SimpleStringProperty()
}
