package com.ironpanthers.scouting.desktop.view.match

import com.ironpanthers.scouting.common.MatchRobotWrapper
import com.ironpanthers.scouting.common.RobotEvent
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleLongProperty
import javafx.geometry.Orientation
import javafx.scene.Parent
import javafx.scene.control.ToggleButton
import org.slf4j.LoggerFactory
import tornadofx.*
import java.util.*

class MatchRobotEditorView(val match: MatchRobotWrapper) : View() {
    override val root: Parent

    private val logger = LoggerFactory.getLogger(javaClass)
    val canBeginRecordProperty = SimpleBooleanProperty(true)
    private val isRecordingProperty = SimpleBooleanProperty(false)
    private val canRecordEventsProperty = SimpleBooleanProperty(false)

    private lateinit var btnDC: ToggleButton
    private val undoStack = LinkedList<RobotEvent>()
    private val redoStack = LinkedList<RobotEvent>()
    private val events = TreeMap<Int, RobotEvent>()
    private val eventTimeline = TimelineView()

    private val initialTimeProperty = SimpleLongProperty()
    private var initialTime by initialTimeProperty

    init {
        val controlPane = PowerUp2018()
        controlPane.editorParent = this
        controlPane.root.disableProperty().bind(isRecordingProperty.and(canRecordEventsProperty).not())
        eventTimeline.initialTimeProperty.bind(initialTimeProperty)

        root = borderpane {
            top = toolbar {
                orientation = Orientation.HORIZONTAL
                togglebutton("Record") {
                    disableProperty().bind(canBeginRecordProperty.not())
                    isSelected = false
                    isRecordingProperty.bind(selectedProperty())
                    initialTime = System.currentTimeMillis()
                }
                btnDC = togglebutton("Robot DC") {
                    enableWhen { isRecordingProperty }
                    canRecordEventsProperty.bind(selectedProperty().not().and(isRecordingProperty))
                }
            }
            center = eventTimeline.root
            bottom = controlPane.root
        }

        isRecordingProperty.onChange {
            logger.info("Recording changed to {}", it)
            btnDC.isSelected = false
        }
    }

    fun onRobotEvent(event: RobotEvent) {
        logger.debug("Received event {}", event)
        undoStack.push(event)
        redoStack.clear()
        eventTimeline.robotEvents.add(event)
    }

    fun undo() {
        val event = undoStack.poll()
        if (event != null) {
            logger.debug("Undoing action {}", event)
            eventTimeline.robotEvents.remove(event)
            redoStack.push(event)
        }
    }

    fun redo() {
        val event = redoStack.poll()
        if (event != null) {
            logger.debug("Redoing action {}", event)
            eventTimeline.robotEvents.add(event)
            undoStack.push(event)
        }
    }

}