package com.ironpanthers.scouting.desktop

import com.ironpanthers.scouting.common.GameDef
import com.ironpanthers.scouting.common.RobotEvent
import com.ironpanthers.scouting.common.RobotEventDef
import com.ironpanthers.scouting.common.RobotPerformance
import com.ironpanthers.scouting.util.UNDO
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.control.RadioButton
import javafx.scene.control.ScrollPane
import javafx.scene.control.ToggleGroup
import javafx.scene.input.KeyEvent
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

    private val endgameToggle = ToggleGroup()
    private var startTime: Long = 0

    var team: Int = 5026

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
            logger.info("setting GameDef to {}", value)
            field = value

            targetButtons.children.apply {
                clear()
                addAll(value?.events?.map { eventDef ->
                    val btn = eventDef.createButton()
                    btn.setOnMouseClicked {
                        logger.debug("triggered event: {}", eventDef)
                        addRobotEvent(eventDef)
                    }
                    btn
                } ?: emptyList())
            }

            endgameToggle.toggles.clear()
            endgameStates.children.apply {
                clear()
                addAll(value?.endStates?.map { endState ->
                    val radio = RadioButton(endState.name)
                    radio.toggleGroup = endgameToggle
                    radio.userData = endState.id
                    radio
                } ?: emptyList())
            }

        }

    @FXML
    fun initialize() {
        logger.info("Initializing")

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

}

data class RobotEventWrapper(val robotEvent: RobotEvent, val label: Label)
