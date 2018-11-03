package com.ironpanthers.scouting.desktop.view

import com.ironpanthers.scouting.common.RobotEvent
import com.ironpanthers.scouting.desktop.util.toFXColor
import com.ironpanthers.scouting.frc2018.GameDef2018
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleLongProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableSet
import javafx.collections.SetChangeListener
import javafx.scene.Parent
import javafx.scene.shape.Circle
import javafx.util.Duration
import org.slf4j.LoggerFactory
import tornadofx.*
import java.util.*


class TimelineView : View() {

    override val root: Parent
    val zoomProperty = SimpleDoubleProperty(1.0)
    var zoom by zoomProperty

    val initialTimeProperty = SimpleLongProperty(0)
    var initialTime by initialTimeProperty

    val robotEvents: ObservableSet<RobotEvent> = FXCollections.observableSet()

    internal val totalTimeProperty = SimpleDoubleProperty(150000.0)
    private val widthProperty = SimpleDoubleProperty()
    internal val heightProperty = SimpleDoubleProperty()
    internal val innerWidthProperty = widthProperty.multiply(zoomProperty)

    private val logger = LoggerFactory.getLogger(javaClass)

    internal val eventRadiusProperty = SimpleDoubleProperty(5.0)
    internal val eventSeparationProperty = eventRadiusProperty.add(1.0)

    private val eventModelSet = TreeSet<RobotEventModel>()
    private val eventModelSetProperty = FXCollections.observableSet(eventModelSet)

    init {
        root = scrollpane {
            heightProperty.bind(heightProperty())
            pane {
                prefWidthProperty().bind(innerWidthProperty)
                bindChildren(eventModelSetProperty, RobotEventModel::circle)
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

        robotEvents.addListener(SetChangeListener<RobotEvent> { change ->
            eventModelSetProperty.remove(RobotEventModel(change.elementAdded, this))
            eventModelSetProperty.add(RobotEventModel(change.elementAdded, this))
        })

    }

}

private class RobotEventModel(val event: RobotEvent, parent: TimelineView) : Comparable<RobotEventModel> {
    val rankProperty = SimpleIntegerProperty(0)
    val rank by rankProperty

    val xProperty = SimpleDoubleProperty()

    val circle: Circle by lazy {
        Circle().apply {
            tooltip(event.type) {
                // disgusting reflection
                try {
                    val fieldBehavior = this@tooltip.javaClass.getDeclaredField("BEHAVIOR")
                    fieldBehavior.isAccessible = true
                    val objBehavior = fieldBehavior.get(this@tooltip)

                    val fieldTimer = objBehavior.javaClass.getDeclaredField("activationTimer")
                    fieldTimer.isAccessible = true
                    val objTimer = fieldTimer.get(objBehavior) as Timeline

                    objTimer.keyFrames.clear()
                    objTimer.keyFrames.add(KeyFrame(Duration(0.0)))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            radiusProperty().bind(parent.eventRadiusProperty)
            fill = GameDef2018.events.find { it.id == event.type }!!.color.toFXColor()  // TODO make it not retard
            centerXProperty().bind(parent.initialTimeProperty.subtract(event.time).multiply(parent.innerWidthProperty).divide(parent.totalTimeProperty).negate())
            centerYProperty().bind(parent.heightProperty.divide(2).add(parent.eventSeparationProperty.multiply(rankProperty)))
            xProperty.bind(centerXProperty())
        }
    }

    override fun compareTo(other: RobotEventModel): Int {
        val a = event.time
        val b = other.event.time
        return when {
            a < b -> -1
            a > b -> 1
            else -> 0
        }
    }

}
