package com.ironpanthers.scouting.desktop.controller

import com.ironpanthers.scouting.desktop.util.ControllerWithStage
import com.ironpanthers.scouting.desktop.util.ViewStageFactory
import com.ironpanthers.scouting.io.server.BaseClient
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.MenuItem
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.layout.Pane
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.util.Callback
import org.slf4j.LoggerFactory
import tornadofx.find

class ServerMonitorController : ControllerWithStage {

    lateinit var stage: Stage

    override fun acceptStage(stage: Stage) {
        this.stage = stage
    }

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
            stage.hide()
            find(CompetitionSelectionView::class).openWindow()
        }
    }

    companion object : ViewStageFactory<ServerMonitorController>("views/server-monitor-view.fxml")

}
