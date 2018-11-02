package com.ironpanthers.scouting.desktop.view

import javafx.beans.property.SimpleDoubleProperty
import javafx.scene.Parent
import org.slf4j.LoggerFactory
import tornadofx.View
import tornadofx.getValue
import tornadofx.pane
import tornadofx.scrollpane
import tornadofx.setValue

class TimelineView : View() {

    override val root: Parent
    val zoomProperty = SimpleDoubleProperty(1.0)

    var zoom by zoomProperty

    private val logger = LoggerFactory.getLogger(javaClass)

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
