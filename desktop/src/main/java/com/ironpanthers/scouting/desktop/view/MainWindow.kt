package com.ironpanthers.scouting.desktop.view

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.ironpanthers.scouting.common.Competition
import com.ironpanthers.scouting.common.MutableCompetition
import com.ironpanthers.scouting.desktop.controller.client.MatchListView
import com.ironpanthers.scouting.desktop.controller.server.CompetitionCreationWizard
import javafx.beans.property.SimpleObjectProperty
import javafx.geometry.Orientation
import javafx.scene.Parent
import javafx.scene.control.Alert
import javafx.scene.layout.Priority
import javafx.stage.FileChooser
import org.slf4j.LoggerFactory
import tornadofx.*

class MainWindow : View() {

    override val root: Parent

    val chatView: ChatView = ChatView()
    val eventLogView = EventLogView()
    val connectionView = ConnectionView()
    val matchListView = MatchListView()

    private val logger = LoggerFactory.getLogger(javaClass)

    private val competitionProperty = SimpleObjectProperty<MutableCompetition?>()
    private var competition: MutableCompetition? by competitionProperty

    init {

        matchListView.competitionProperty.bind(competitionProperty)

        root = borderpane {
            top = menubar {
                menu("File") {
                    item("New") {
                        action {
                            val wizard = CompetitionCreationWizard()
                            wizard.openModal(block = true)
                            competition = wizard.result?.asMutable()
                            logger.debug("competition is now set to {}", competition)
                        }
                    }
                    item("Open...") {
                        action {
                            logger.info("Opening 'Open' menu")
                            val chooser = FileChooser()
                            chooser.extensionFilters.addAll(
                                    FileChooser.ExtensionFilter("JSON File", "*.json"),
                                    FileChooser.ExtensionFilter("All Files", "*.*")
                            )
                            chooser.title = "Select competition data..."
                            val file = chooser.showOpenDialog(currentWindow)
                            logger.debug("got file: {}", file)
                            if (file == null) {
                                logger.debug("no file was selected")
                                return@action
                            }

                            try {
                                competition = jacksonObjectMapper().readValue(file)
                                logger.debug("loaded competition data: {}", competition)
                            } catch (e: Exception) {
                                logger.warn("Error while attempting to load file!", e)
                                Alert(Alert.AlertType.ERROR).apply {
                                    title = "Could not read file!"
                                    headerText = "We could not parse the file provided."
                                    contentText = e.localizedMessage
                                    showAndWait()
                                }
                            }
                        }
                    }
                    item("Save") {
                        disableWhen { competitionProperty.isNull }
                    }
                    item("Save As...") {
                        disableWhen { competitionProperty.isNull }
                        action {
                            logger.info("Opening 'Save As' menu")
                            val chooser = FileChooser()
                            chooser.extensionFilters.addAll(
                                    FileChooser.ExtensionFilter("JSON File", "*.json"),
                                    FileChooser.ExtensionFilter("All Files", "*.*")
                            )
                            chooser.title = "Save competition data..."
                            val file = chooser.showSaveDialog(currentWindow)
                            logger.debug("dest file: {}", file)
                            if (file == null) {
                                logger.debug("no file was selected")
                                return@action
                            }

                            try {
                                jacksonObjectMapper().writeValue(file, competition)
                                logger.debug("wrote competition data: {}", competition)
                            } catch (e: Exception) {
                                logger.warn("Error while attempting to save file!", e)
                                Alert(Alert.AlertType.ERROR).apply {
                                    title = "Could not save file!"
                                    headerText = "We could not save the file."
                                    contentText = e.localizedMessage
                                    showAndWait()
                                }
                            }
                        }
                    }
                }
                menu("Edit") {

                }
            }

            left = toolbar {
                //alignment = Pos.TOP_LEFT
                orientation = Orientation.VERTICAL
                group {
                    togglebutton("Matches") {
                        rotate = -90.0
                    }
                }
            }

            right = toolbar {
                //alignment = Pos.TOP_RIGHT
                orientation = Orientation.VERTICAL
                group {
                    togglebutton("Connection") {
                        rotate = 90.0
                    }
                }
            }

            center = splitpane(Orientation.VERTICAL) {
                splitpane(Orientation.HORIZONTAL) {
                    hbox {
                        vgrow = Priority.ALWAYS
                        pane {
                            add(matchListView)
                        }
                    }

                    pane {

                    }

                    hbox {
                        vgrow = Priority.ALWAYS
                        pane {

                        }
                    }
                }

                vbox {
                    vgrow = Priority.ALWAYS
                    prefHeight = 0.0
                    splitpane(Orientation.HORIZONTAL) {
                        add(chatView)
                        add(eventLogView)
                    }
                }

            }

            bottom = toolbar {
                //alignment = Pos.BOTTOM_LEFT
                val btnChat = togglebutton("Chat")
                val btnEventLog = togglebutton("Event Log")

                chatView.root.visibleProperty().bind(btnChat.selectedProperty())
                eventLogView.root.visibleProperty().bind(btnEventLog.selectedProperty())
            }

        }

        competitionProperty.onChange {
            when (it) {
                null -> eventLogView.appendMessage("Closed last competition")
                else -> eventLogView.appendMessage("Competition changed to ${it.name}")
            }
        }
    }

}
