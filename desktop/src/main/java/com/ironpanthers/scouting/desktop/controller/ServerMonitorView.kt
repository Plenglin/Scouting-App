package com.ironpanthers.scouting.desktop.controller

import com.ironpanthers.scouting.common.Competition
import com.ironpanthers.scouting.io.server.BaseClient
import com.ironpanthers.scouting.io.server.ServerEngine
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.Parent
import org.slf4j.LoggerFactory
import tornadofx.*

class ServerMonitorView : View() {

    override val root: Parent
    private val logger = LoggerFactory.getLogger(javaClass)

    private val serverEnabledProperty = SimpleBooleanProperty(false)
    private var serverEnabled by serverEnabledProperty

    init {
        root = stackpane {
            prefWidth = 800.0
            prefHeight = 600.0
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
                                serverEnabled = true
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
                                serverEnabled = true
                            }
                        }
                        openWindow()
                    }
                }
            }

            borderpane {
                visibleProperty().bind(serverEnabledProperty)
                center = tableview<BaseClient> {
                    column<BaseClient, String>("Client") {
                        SimpleStringProperty(it.value.displayName)
                    }
                    column<BaseClient, String>("Type") {
                        SimpleStringProperty(it.value.type)
                    }
                    column<BaseClient, Boolean>("Connected") {
                        SimpleObjectProperty<Boolean>(it.value.connected)
                    }
                    contextmenu {
                        menu("Kick") {
                            action {
                                logger.info("Kicking")

                            }
                        }
                    }
                }
                right {
                    button("Stop") {
                        action {
                            ServerEngine.stop()
                            serverEnabled = false
                        }
                    }
                }
            }

        }
    }
}
