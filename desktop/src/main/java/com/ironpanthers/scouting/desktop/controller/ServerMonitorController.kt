package com.ironpanthers.scouting.desktop.controller

import com.ironpanthers.scouting.desktop.io.server.LocalClient
import com.ironpanthers.scouting.io.server.Client
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.fxml.FXML
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.util.Callback
import org.slf4j.LoggerFactory

class ServerMonitorController {

    @FXML
    lateinit var clients: TableView<Client>

    @FXML
    lateinit var colNames: TableColumn<Client, String>
    @FXML
    lateinit var colType: TableColumn<Client, String>
    @FXML
    lateinit var colStatus: TableColumn<Client, Boolean>

    private val log = LoggerFactory.getLogger(javaClass)

    @FXML
    fun initialize() {
        clients.items.add(LocalClient())
        colNames.cellValueFactory = Callback {
            SimpleStringProperty(it.value.displayName)
        }
        colType.cellValueFactory = Callback {
            SimpleStringProperty(it.value.type)
        }
        colStatus.cellValueFactory = Callback {
            SimpleBooleanProperty(it.value.connected)
        }
    }

}
