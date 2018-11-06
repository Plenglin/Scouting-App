package com.ironpanthers.scouting.desktop.view

import com.ironpanthers.scouting.desktop.io.server.BluetoothServer
import javafx.beans.property.ReadOnlyBooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.scene.Parent
import org.controlsfx.control.ToggleSwitch
import tornadofx.*

class ConnectionView : View() {

    override val root: Parent
    val serverEnabledProperty = SimpleBooleanProperty()

    init {
        root = vbox {
            toolbar {
                add(ToggleSwitch("Master").apply {
                    serverEnabledProperty.bind(selectedProperty())
                })
                button("+")
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
    }

    private fun startServer() {
        BluetoothServer.start()
    }

    private fun stopServer() {
        BluetoothServer.close()
    }
}