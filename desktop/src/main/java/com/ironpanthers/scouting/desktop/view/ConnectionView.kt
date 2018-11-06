package com.ironpanthers.scouting.desktop.view

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ironpanthers.scouting.desktop.io.server.BluetoothServer
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.Parent
import javafx.scene.control.TreeItem
import org.controlsfx.control.ToggleSwitch
import org.slf4j.LoggerFactory
import tornadofx.*
import javax.bluetooth.RemoteDevice

class ConnectionView : View() {

    override val root: Parent
    val serverEnabledProperty = SimpleBooleanProperty()
    private val logger = LoggerFactory.getLogger(javaClass)
    private val server = BluetoothServer()
    private val mapper = jacksonObjectMapper()
    private val peers = observableList<DeviceConnection>()

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
                            peers.add(DeviceConnection(dev))
                        }
                        //logger.debug("Connecting to URL {}", wizard.result?.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false))
                    }
                }
                button("Refresh")
            }
            val rootTreeItem = TreeItem<String>()
            rootTreeItem.isExpanded = true
            rootTreeItem.children.bind(peers) { dev ->
                TreeItem(dev.dev.bluetoothAddress).apply {
                    userData = dev
                    treeitem("Connect Chat Relay")
                    treeitem("Connect Match Manager")
                }
            }
            treetableview(rootTreeItem) {
                isShowRoot = false
                column<String, String>("Name") {
                    it.value.valueProperty()
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

        server.endpoint("robot-performance") {
            //mapper.treeToValue()
        }
    }

    private fun startServer() {
        server.start()
    }

    private fun stopServer() {
        server.close()
    }
}

data class DeviceConnection(val dev: RemoteDevice) {
    val nameProperty = SimpleStringProperty(dev.bluetoothAddress)
    val isConnectedProperty = SimpleBooleanProperty(false)
}
