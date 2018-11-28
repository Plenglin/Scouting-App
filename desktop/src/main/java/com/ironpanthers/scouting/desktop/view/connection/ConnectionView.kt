package com.ironpanthers.scouting.desktop.view.connection

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.intel.bluetooth.MicroeditionConnector
import com.ironpanthers.scouting.desktop.io.BluetoothPeer
import com.ironpanthers.scouting.desktop.io.PeerDiscoveryManager
import com.ironpanthers.scouting.desktop.io.match.server.BluetoothMatchServer
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
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
    private val mapper = jacksonObjectMapper()
    private val peers = observableList<BluetoothPeer>()

    init {
        peers.bind(PeerDiscoveryManager.peers) { it }
        root = vbox {
            toolbar {
                add(ToggleSwitch("Master").apply {
                    serverEnabledProperty.bind(selectedProperty())
                })
                button("Refresh devices") {
                    action {
                        PeerDiscoveryManager.refreshPairedDevices()
                    }
                }
                button("Refresh services") {
                    action {
                        peers.forEach {
                            it.findServices()
                        }
                    }
                }
                button("Add unpaired device") {
                    action {
                        logger.info("Opening a PDD")
                        val pdd = find<PeerDiscoveryDialog>()
                        pdd.openWindow(block = true, escapeClosesWindow = true)

                        val res = pdd.consumeResult()
                        logger.debug("PDD returned result {}", res)
                        PeerDiscoveryManager.peers.add(res)
                    }
                }
            }

            val rootTreeItem = TreeItem<ConnectionTreeNode>(Root())
            rootTreeItem.isExpanded = true
            rootTreeItem.children.bind(peers) { dev ->
                val wrapper = Device(dev)
                val tree = TreeItem<ConnectionTreeNode>(wrapper)
                tree.children.bind(wrapper.children) { TreeItem(it) }
                tree
            }

            treeview(rootTreeItem) {
                isShowRoot = false
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
    }

    private fun findServices() {

    }

    private fun startServer() {
        BluetoothMatchServer.start()
    }

    private fun stopServer() {
        BluetoothMatchServer.stop()
    }
}

data class DeviceNodeWrapper(val dev: RemoteDevice) {
    val nameProperty = SimpleStringProperty(dev.bluetoothAddress)
}

sealed class ConnectionTreeNode {
    abstract val nameProperty: StringProperty

    override fun toString(): String {
        return nameProperty.get()
    }
}

private class Root : ConnectionTreeNode() {
    override val nameProperty: StringProperty = SimpleStringProperty()
}

private class Device(dev: BluetoothPeer) : ConnectionTreeNode() {
    private val logger = LoggerFactory.getLogger(javaClass)

    override val nameProperty: StringProperty = SimpleStringProperty(dev.device.getFriendlyName(true))
    val isSearchingProperty = SimpleBooleanProperty(false)

    var foundMatch: ServiceRecord? = null
    var foundChat: ServiceRecord? = null

    val children = observableList<ConnectionTreeNode>()

    init {
        isSearchingProperty.bind(dev.searchingProperty)
        isSearchingProperty.onChange { searching ->
            logger.debug("{} search status changed to {}", dev, searching)
            if (searching) {
                children.setAll(Text("Loading..."))
            } else {
                children.clear()
                foundChat?.let {
                    children.add(Chat(it))
                }
                foundMatch?.let {
                    children.add(MatchMaster(it))
                }
                if (children.isEmpty()) {
                    children.add(Text("No valid services found"))
                }
            }
        }
    }
}

private class Text(text: String) : ConnectionTreeNode() {
    override val nameProperty: StringProperty = SimpleStringProperty(text)
}

private class Chat(val serviceRecord: ServiceRecord) : ConnectionTreeNode() {
    val connectedProperty = SimpleBooleanProperty(false)
    override val nameProperty: StringProperty = SimpleStringProperty("Chat Relay")
}

private class MatchMaster(val serviceRecord: ServiceRecord) : ConnectionTreeNode() {
    val connectedProperty = SimpleBooleanProperty(false)
    override val nameProperty: StringProperty = SimpleStringProperty()
}
