package com.ironpanthers.scouting.desktop.view

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.ironpanthers.scouting.common.Competition
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

    private val logger = LoggerFactory.getLogger(javaClass)
    private var competition: Competition? = null

    init {

        root = borderpane {
            top = menubar {
                menu("File") {
                    item("New")
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
    }

}
