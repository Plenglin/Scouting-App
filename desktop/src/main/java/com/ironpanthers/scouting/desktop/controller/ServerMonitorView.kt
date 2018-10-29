package com.ironpanthers.scouting.desktop.controller

import com.ironpanthers.scouting.desktop.util.ViewStageFactory
import com.ironpanthers.scouting.io.server.BaseClient
import com.ironpanthers.scouting.io.server.ServerEngine
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.fxml.FXML
import javafx.scene.Parent
import javafx.scene.control.Button
import javafx.scene.control.MenuItem
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.layout.Pane
import javafx.util.Callback
import org.slf4j.LoggerFactory
import tornadofx.*

class ServerMonitorView : View() {

    override val root: Parent
    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        root = stackpane {
            vbox {
                label("Server is not on!")
                button("Load competition") {
                    action {
                        logger.debug("User wants to use an existing competition")
                        modalStage?.hide()
                        CompetitionSelectionView().let {
                            it.openWindow(block = true)
                            val rs = it.result
                            logger.debug("Received result {}", rs)
                            if (rs != null) {
                                ServerEngine.start(rs.id)
                            }
                        }
                        openWindow()
                    }
                }
                button("New competition") {
                    action {
                        logger.debug("User wants to create a new competition")
                        modalStage?.hide()
                        CompetitionCreationWizard().let {
                            it.openWindow(block = true)
                            val rs = it.result
                            logger.debug("Received result {}", rs)
                            if (rs != null) {
                                ServerEngine.start(rs.id)
                            }
                        }
                        openWindow()
                    }
                }
            }

            borderpane {
                isVisible = false
                center = tableview<BaseClient> {
                    /*colNames.cellValueFactory = Callback {
                        SimpleStringProperty(it.value.displayName)
                    }
                    colType.cellValueFactory = Callback {
                        SimpleStringProperty(it.value.type)
                    }
                    colStatus.cellValueFactory = Callback {
                        SimpleBooleanProperty(it.value.connected)
                    }
                    btnSelectCompetition.setOnMouseClicked {
                        find(CompetitionSelectionView::class).openWindow()
                    }*/

                }
            }

        }
    }
}
