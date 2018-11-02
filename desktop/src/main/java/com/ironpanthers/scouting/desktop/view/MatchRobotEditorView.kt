package com.ironpanthers.scouting.desktop.view

import com.ironpanthers.scouting.common.MatchRobotWrapper
import javafx.beans.property.SimpleBooleanProperty
import javafx.geometry.Orientation
import javafx.scene.Parent
import javafx.scene.control.ToggleButton
import org.slf4j.LoggerFactory
import tornadofx.*

class MatchRobotEditorView(val match: MatchRobotWrapper) : View() {
    override val root: Parent

    private val logger = LoggerFactory.getLogger(javaClass)
    private val isRecordingProperty = SimpleBooleanProperty()
    private val canRecordEventsProperty = SimpleBooleanProperty()

    private lateinit var btnDC: ToggleButton

    init {
        root = borderpane {
            top = toolbar {
                orientation = Orientation.HORIZONTAL
                togglebutton("Record") {
                    isSelected = false
                    isRecordingProperty.bind(selectedProperty())
                }
                btnDC = togglebutton("Robot DC") {
                    enableWhen { isRecordingProperty }
                    canRecordEventsProperty.bind(selectedProperty().not().and(isRecordingProperty))
                }
            }
        }

        isRecordingProperty.onChange {
            logger.info("Recording changed to {}", it)
            btnDC.isSelected = false
        }
    }

}