package com.ironpanthers.scouting.desktop

import com.ironpanthers.scouting.common.GameDef
import com.ironpanthers.scouting.common.RobotEvent
import com.ironpanthers.scouting.common.RobotEventDef
import com.ironpanthers.scouting.common.RobotPerformance
import com.ironpanthers.scouting.util.UNDO
import javafx.beans.property.SimpleBooleanProperty
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent
import javafx.scene.layout.FlowPane
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import org.slf4j.LoggerFactory
import java.util.*

class ScoutingController {

    private val logger = LoggerFactory.getLogger(javaClass)
    private val timer = Timer()

    private val events: LinkedList<RobotEvent> = LinkedList()

    @FXML private lateinit var root: Pane
    @FXML private lateinit var targetButtons: FlowPane
    @FXML private lateinit var robotTimelinePane: ScrollPane
    @FXML private lateinit var endgameStates: VBox
    @FXML private lateinit var btnStart: Button
    @FXML private lateinit var btnStop: Button
    //@FXML private lateinit var btnFieldError: Button

    private val eventButtons: MutableList<Button> = mutableListOf()

    private val endgameToggle = ToggleGroup()
    private var startTime: Long = 0

    var team: Int = 5026

    private val isRecordingProperty = SimpleBooleanProperty(false)
    private val isStoppedProperty = isRecordingProperty.not()
    private var isRecording by isRecordingProperty

    @FXML
    fun initialize() {
        logger.info("Initializing")
        btnStart.disableProperty().bind(isRecordingProperty)
        btnStop.disableProperty().bind(isStoppedProperty)
        //btnFieldError.disableProperty().bind(isStoppedProperty)
    }


    private fun addRobotEvent(def: RobotEventDef) {
        val event = def.createEventInstance(team)
        events.push(event)
        println(events)
    }

    private fun undoRobotEvent() {
        if (!events.isEmpty()) {
            events.pop()
        }
    }

    fun createRobotPerformance(): RobotPerformance {
        return RobotPerformance(team, startTime, events.sortedBy { it.time }, endgameToggle.selectedToggle.userData as String)
    }

    var gameDef: GameDef? = null
        set(value) {
            if (isRecording) throw IllegalStateException("Cannot change gameDef while recording!")
            logger.info("setting GameDef to {}", value)
            field = value
            value?.let { createGameDefButtons(it) }
        }

    private fun createGameDefButtons(value: GameDef) {
        val events = value.events.map { eventDef ->
            val btn = eventDef.createButton()
            btn.setOnMouseClicked {
                logger.debug("triggered event: {}", eventDef)
                addRobotEvent(eventDef)
            }
            btn.disableProperty().bind(isStoppedProperty)
            btn
        }
        val endings = value.endStates.map { endState ->
            val radio = RadioButton(endState.name)
            radio.toggleGroup = endgameToggle
            radio.userData = endState.id
            radio
        }

        eventButtons.apply {
            clear()
            addAll(events)
        }

        targetButtons.children.apply {
            clear()
            addAll(events)
        }

        endgameToggle.toggles.clear()
        endgameStates.children.apply {
            clear()
            addAll(endings)
        }
    }

    @FXML
    fun shutdown() {
        logger.info("Stopping")
    }

    fun handleOnKeyPressed(keyEvent: KeyEvent) {
        logger.debug("key event: {}", keyEvent)
        if (UNDO.test(keyEvent)) {
            undoRobotEvent()
        }
        gameDef?.apply {
            events.find { it.keyCombo.test(keyEvent) }?.let { eventDef ->
                logger.debug("selected: {}", eventDef)
                addRobotEvent(eventDef)
            }
        }
    }

    fun onBtnRecordPressed(event: MouseEvent) {
        if (isRecording) throw IllegalStateException("Already recording!")
        logger.info("Begin recording")
        isRecording = true
    }

    fun onBtnStopPressed(event: MouseEvent) {
        if (!isRecording) throw IllegalStateException("Not recording!")
        logger.info("Stopped recording")
        isRecording = false
    }

}

data class RobotEventWrapper(val robotEvent: RobotEvent, val label: Label)
