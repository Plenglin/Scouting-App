package com.ironpanthers.scouting.desktop.view

import com.ironpanthers.scouting.common.RobotEvent
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleLongProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.shape.Circle
import org.slf4j.LoggerFactory
import tornadofx.*
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.util.Duration
import java.lang.reflect.AccessibleObject.setAccessible



class TimelineView : View() {

    override val root: Parent
    val zoomProperty = SimpleDoubleProperty(1.0)
    var zoom by zoomProperty

    val initialTimeProperty = SimpleLongProperty(0)
    var initialTime by initialTimeProperty

    val robotEvents: ObservableList<RobotEvent> = FXCollections.observableArrayList()

    private val totalTimeProperty = SimpleDoubleProperty(150000.0)
    private val widthProperty = SimpleDoubleProperty()
    private val heightProperty = SimpleDoubleProperty()
    private val innerWidthProperty = widthProperty.multiply(zoomProperty)

    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        root = scrollpane {
            heightProperty.bind(heightProperty())
            pane {
                prefWidthProperty().bind(innerWidthProperty)
                bindChildren(robotEvents) { ev ->
                    val circle = Circle().apply {
                        tooltip(ev.type) {
                            // disgusting reflection
                            try {
                                val fieldBehavior = this@tooltip.javaClass.getDeclaredField("BEHAVIOR")
                                fieldBehavior.isAccessible = true
                                val objBehavior = fieldBehavior.get(tooltip)

                                val fieldTimer = objBehavior.javaClass.getDeclaredField("activationTimer")
                                fieldTimer.isAccessible = true
                                val objTimer = fieldTimer.get(objBehavior) as Timeline

                                objTimer.keyFrames.clear()
                                objTimer.keyFrames.add(KeyFrame(Duration(250.0)))
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        radius = 10.0
                        centerXProperty().bind(initialTimeProperty.subtract(ev.time).multiply(innerWidthProperty).divide(totalTimeProperty).negate())
                        centerYProperty().bind(heightProperty.divide(2))
                        logger.trace("circle at ({}, {})", centerX, centerY)
                    }
                    logger.trace("converting {} to {}", ev, circle)
                    circle
                }
            }

            setOnScroll {
                logger.trace("received scrollevent {}", it)
                if (it.isControlDown) {
                    zoom *= if (it.deltaY > 0) 1.33 else 0.75
                    logger.debug("zoom set to {}", zoom)
                }
                it.consume()
            }
        }
        widthProperty.bind(root.widthProperty())

    }

}
