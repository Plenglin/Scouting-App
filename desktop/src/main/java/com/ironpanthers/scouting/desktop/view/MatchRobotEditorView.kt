package com.ironpanthers.scouting.desktop.view

import com.ironpanthers.scouting.common.MatchRobotWrapper
import com.ironpanthers.scouting.common.RobotEvent
import javafx.beans.property.SimpleBooleanProperty
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

    init {
        val controlPane = PowerUp2018()
        controlPane.editorParent = this
        root = borderpane {
            top = toolbar {
                orientation = Orientation.HORIZONTAL
                togglebutton("Record") {
                    disableProperty().bind(canBeginRecordProperty.not())
                    isSelected = false
                    isRecordingProperty.bind(selectedProperty())
                }
                btnDC = togglebutton("Robot DC") {
                    enableWhen { isRecordingProperty }
                    canRecordEventsProperty.bind(selectedProperty().not().and(isRecordingProperty))
                }
            }
            center = controlPane.root
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
    }

    fun undo() {
        val event = undoStack.pop()
        logger.debug("Undid action {}", event)
    }

    fun redo() {
        val obj = redoStack.pop()
        if (obj != null) {
            logger.debug("Redoing")
            undoStack.push(obj)
        }
    }

}