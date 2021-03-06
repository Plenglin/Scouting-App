package com.ironpanthers.scouting.desktop.view

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.ironpanthers.scouting.common.MutableCompetition
import com.ironpanthers.scouting.desktop.controller.server.CompetitionCreationWizard
import com.ironpanthers.scouting.desktop.view.chat.ChatView
import com.ironpanthers.scouting.desktop.view.connection.ConnectionView
import com.ironpanthers.scouting.desktop.view.match.MatchListView
import com.ironpanthers.scouting.desktop.view.match.MatchRobotEditorView
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.geometry.Orientation
import javafx.scene.Parent
import javafx.scene.control.Alert
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.layout.Priority
import javafx.stage.FileChooser
import org.slf4j.LoggerFactory
import tornadofx.*
import java.io.File

class MainWindow : View() {

    override val root: Parent

    val chatView: ChatView = ChatView()
    val eventLogView = EventLogView()
    val connectionView = ConnectionView()
    val matchListView = MatchListView()

    private val logger = LoggerFactory.getLogger(javaClass)

    private val competitionProperty = SimpleObjectProperty<MutableCompetition?>()
    private var competition: MutableCompetition? by competitionProperty
    private var saveDest: File? = null

    // For when it's being used as a slave
    private val areMatchesEditableProperty = SimpleBooleanProperty(true)
    private val editableMatchProperty = SimpleObjectProperty<Tab>(null)

    private lateinit var editorTabPane: TabPane

    init {
        matchListView.competitionProperty.bind(competitionProperty)
        matchListView.onRobotSelected = {
            logger.info("Opening editor for {}", it)
            val editor = MatchRobotEditorView(it)
            val tab = Tab("#${it.parent.number}: ${it.robot.team}", editor.root)
            editor.canBeginRecordProperty.bind(areMatchesEditableProperty)
            tab.userData = editor
            editorTabPane.tabs.add(tab)
            editorTabPane.selectionModel.select(tab)

            tab.closableProperty().bind(editableMatchProperty.isNotEqualTo(tab))
            eventLogView.appendMessage("Opened Match ${it.parent.number} Team ${it.robot.team}") {
                editorTabPane.selectionModel.select(tab)
            }
        }

        root = borderpane {
            top = menubar {
                menu("File") {
                    item("New") {
                        action { doCreateNew() }
                    }
                    item("Open...") {
                        action { doOpen() }
                    }
                    item("Save") {
                        disableWhen { competitionProperty.isNull }
                        action {
                            logger.info("Attempting to save file")
                            doSave()
                        }
                    }
                    item("Save As...") {
                        disableWhen { competitionProperty.isNull }
                        action {
                            chooseSaveDest()
                            doSave()
                        }
                    }
                }
                menu("Edit") {
                    item("Undo") {
                        action {
                            val current = editorTabPane.selectionModel.selectedItem.userData
                            when (current) {
                                is MatchRobotEditorView -> current.undo()
                            }
                        }
                    }
                    item("Redo") {
                        action {
                            val current = editorTabPane.selectionModel.selectedItem.userData
                            when (current) {
                                is MatchRobotEditorView -> current.redo()
                            }
                        }
                    }
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
                    vgrow = Priority.ALWAYS
                    add(matchListView)

                    editorTabPane = tabpane()

                    add(connectionView)
                }

                vgrow = Priority.ALWAYS
                prefHeight = 0.0
                splitpane(Orientation.HORIZONTAL) {
                    vgrow = Priority.ALWAYS
                    /*(chatView.root as Region).apply {
                        prefHeightProperty().bind(heightProperty())
                        vgrow = Priority.ALWAYS
                    }
                    (eventLogView.root as Region).apply {
                        prefHeightProperty().bind(heightProperty())
                        vgrow = Priority.ALWAYS
                    }*/
                    add(chatView)
                    add(eventLogView)
                }

            }

            bottom = toolbar {
                //alignment = Pos.BOTTOM_LEFT
                val btnChat = togglebutton("Chat")
                val btnEventLog = togglebutton("Event Log")

                chatView.root.apply {
                    hgrow = Priority.ALWAYS
                    visibleProperty().bind(btnChat.selectedProperty())
                }
                eventLogView.root.apply {
                    hgrow = Priority.ALWAYS
                    visibleProperty().bind(btnEventLog.selectedProperty())
                }
            }

        }

        competitionProperty.onChange {
            when (it) {
                null -> eventLogView.appendMessage("Closed last competition")
                else -> eventLogView.appendMessage("Competition changed to ${it.name}")
            }
        }
        connectionView.serverEnabledProperty.onChange {
            eventLogView.appendMessage(if (it) "Server enabled" else "Server disabled")
        }
    }

    private fun doCreateNew() {
        val wizard = CompetitionCreationWizard()
        wizard.openModal(block = true)
        competition = wizard.result?.asMutable()
        logger.debug("competition is now set to {}", competition)
    }

    private fun doOpen() {
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
            return
        }

        try {
            competition = jacksonObjectMapper().readValue(file)
            saveDest = file
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

    private fun doSave() {
        if (saveDest == null) {
            logger.debug("We do not have a save destination")
            chooseSaveDest()
        }
        try {
            logger.debug("file is {}", saveDest)
            jacksonObjectMapper().writeValue(saveDest, competition)
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

    private fun chooseSaveDest() {
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
        } else {
            saveDest = file
        }
    }

}
