package com.ironpanthers.scouting.desktop.controller

import com.ironpanthers.scouting.common.GameDef
import com.ironpanthers.scouting.common.RobotEvent
import com.ironpanthers.scouting.common.RobotEventDef
import com.ironpanthers.scouting.common.MatchRobot
import com.ironpanthers.scouting.desktop.getFXViewData
import com.ironpanthers.scouting.desktop.test
import com.ironpanthers.scouting.util.UNDO
import javafx.beans.property.SimpleBooleanProperty
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import org.slf4j.LoggerFactory
import java.util.*
import tornadofx.*

class ScoutingController {

    private val log = LoggerFactory.getLogger(javaClass)
    private val timer = Timer()

    private val events: LinkedList<RobotEvent> = LinkedList()

    @FXML private lateinit var root: Pane
    @FXML private lateinit var gameDefTarget: Pane
    @FXML private lateinit var endgameStates: VBox
    @FXML private lateinit var btnStart: Button
    @FXML private lateinit var btnStop: Button

    private val endgameToggle = ToggleGroup()
    private var startTime: Long = 0

    var team: Int = 5026

    private val isRecordingProperty = SimpleBooleanProperty(false)
    private val isStoppedProperty = isRecordingProperty.not()
    private var isRecording by isRecordingProperty

    @FXML
    fun initialize() {
        log.info("Initializing")
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

    /*fun createRobotPerformance(): MatchRobot {
        return MatchRobot(team, startTime, events.sortedBy { it.time }, endgameToggle.selectedToggle.userData as String)
    }*/

    var gameDef: GameDef? = null
        set(value) {
            if (isRecording) throw IllegalStateException("Cannot change gameDef while recording!")
            log.info("setting GameDef to {}", value)
            field = value
            value?.apply {
                createGameDefButtons(this)
                val loader = getFXViewData()!!
                val pane = loader.load<Pane>()
                val controller = loader.getController<RobotEventPanelController>()
                controller.onEventOccurred = { onReceivedRobotEvent(it) }
                controller.teamNumber = team

                gameDefTarget.children.add(pane)
            }
        }

    private fun onReceivedRobotEvent(event: RobotEvent) {
        if (isRecording) {
            log.info("Received robot event {}", event)
            events.push(event)
        } else {
            log.info("Did not push {} (not recording!)", event)
        }
    }

    private fun createGameDefButtons(value: GameDef) {
        val endings = value.endStates.map { endState ->
            val radio = RadioButton(endState.name)
            radio.toggleGroup = endgameToggle
            radio.userData = endState.id
            radio
        }
        endgameToggle.toggles.clear()
        endgameStates.children.apply {
            clear()
            addAll(endings)
        }
    }

    fun shutdown() {
        log.info("Stopping")
    }

    fun handleOnKeyPressed(keyEvent: KeyEvent) {
        log.debug("key event: {}", keyEvent)
        if (UNDO.test(keyEvent)) {
            undoRobotEvent()
        }
        gameDef?.apply {
            events.find { it.keyCombo.test(keyEvent) }?.let { eventDef ->
                log.debug("selected: {}", eventDef)
                addRobotEvent(eventDef)
            }
        }
    }

    fun onBtnRecordPressed(event: MouseEvent) {
        if (isRecording) throw IllegalStateException("Already recording!")
        log.info("Begin recording")
        isRecording = true
    }

    fun onBtnStopPressed(event: MouseEvent) {
        if (!isRecording) throw IllegalStateException("Not recording!")
        log.info("Stopped recording")
        isRecording = false
    }

}

data class RobotEventWrapper(val robotEvent: RobotEvent, val label: Label)
