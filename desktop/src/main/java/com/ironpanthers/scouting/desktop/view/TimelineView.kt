package com.ironpanthers.scouting.desktop.view

import com.ironpanthers.scouting.common.RobotEvent
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleLongProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.Node
import javafx.scene.Parent
import org.slf4j.LoggerFactory
import tornadofx.*

class TimelineView : View() {

    override val root: Parent
    val zoomProperty = SimpleDoubleProperty(1.0)
    var zoom by zoomProperty

    val initialTimeProperty = SimpleLongProperty(0)
    var initialTime by initialTimeProperty

    val robotEvents: ObservableList<RobotEvent> = FXCollections.observableArrayList()

    private val logger = LoggerFactory.getLogger(javaClass)
    private val nodes = mutableMapOf<RobotEvent, Node>()

    init {
        root = scrollpane {
            pane {
                prefWidthProperty().bind(this@scrollpane.widthProperty().multiply(zoomProperty))
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

    }

}
