package com.ironpanthers.scouting.desktop.controller

import com.ironpanthers.scouting.io.server.BaseClient
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.MenuItem
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.layout.Pane
import javafx.stage.Stage
import javafx.util.Callback
import org.slf4j.LoggerFactory

class ServerMonitorController {

    @FXML
    lateinit var clients: TableView<BaseClient>

    @FXML
    lateinit var colNames: TableColumn<BaseClient, String>
    @FXML
    lateinit var colType: TableColumn<BaseClient, String>
    @FXML
    lateinit var colStatus: TableColumn<BaseClient, Boolean>
    @FXML
    lateinit var btnSelectCompetition: Button

    @FXML
    lateinit var paneServerEnabled: Pane
    @FXML
    lateinit var menuKick: MenuItem

    val serverIsRunningProperty: SimpleBooleanProperty = SimpleBooleanProperty(true)

    private val log = LoggerFactory.getLogger(javaClass)

    @FXML
    fun initialize() {
        val cl = javaClass.classLoader

        colNames.cellValueFactory = Callback {
            SimpleStringProperty(it.value.displayName)
        }
        colType.cellValueFactory = Callback {
            SimpleStringProperty(it.value.type)
        }
        colStatus.cellValueFactory = Callback {
            SimpleBooleanProperty(it.value.connected)
        }
        btnSelectCompetition.setOnMouseClicked {
            (btnSelectCompetition.scene as Stage).hide()
            Stages.competitionSelection.showAndWait()
        }
    }

}
