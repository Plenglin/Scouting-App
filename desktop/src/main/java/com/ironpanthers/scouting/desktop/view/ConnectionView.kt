package com.ironpanthers.scouting.desktop.view

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ironpanthers.scouting.desktop.io.server.BluetoothServer
import javafx.beans.property.SimpleBooleanProperty
import javafx.scene.Parent
import org.controlsfx.control.ToggleSwitch
import org.slf4j.LoggerFactory
import tornadofx.*

class ConnectionView : View() {

    override val root: Parent
    val serverEnabledProperty = SimpleBooleanProperty()
    private val logger = LoggerFactory.getLogger(javaClass)
    private val server = BluetoothServer()
    private val mapper = jacksonObjectMapper()

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
                        logger.info("Got device {}", wizard.result)
                        //logger.debug("Connecting to URL {}", wizard.result?.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false))
                    }
                }
                button("Refresh")
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