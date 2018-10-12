package com.ironpanthers.scouting.desktop

import javafx.beans.binding.DoubleExpression
import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.canvas.Canvas
import javafx.scene.control.ScrollBar
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.scene.text.TextAlignment
import org.slf4j.LoggerFactory
import tornadofx.getValue
import tornadofx.onChange
import tornadofx.setValue

class TimelineController {

    @FXML lateinit var backgroundMarkings: Canvas
    @FXML lateinit var labelLayer: Pane
    @FXML lateinit var scrollBar: ScrollBar  // It will have the value of t0

    private val totalTimeExpression = SimpleDoubleProperty(MATCH_TIME_SECONDS)
    private val visibleWindowProperty = SimpleDoubleProperty(MATCH_TIME_SECONDS)
    private var visibleTimeWindow by visibleWindowProperty

    @FXML
    fun initialize() {
        scrollBar.minProperty().set(0.0)
        scrollBar.maxProperty().bind(visibleWindowProperty)
        scrollBar.visibleAmountProperty().bind(visibleWindowProperty)

        scrollBar.valueProperty().onChange {
            log.debug("dragged scrollbar to t0={}", it)
            redrawMarkings()
        }
        backgroundMarkings.setOnScroll {
            if (it.isControlDown) {
                val delta = Math.exp(Math.signum(it.deltaY) * -0.3)
                visibleTimeWindow = (visibleTimeWindow * delta).coerceAtMost(MATCH_TIME_SECONDS)
                log.debug("zooming to {}", visibleTimeWindow)
                labelLayer.prefWidth *= delta
                redrawMarkings()
            }
        }
        redrawMarkings()
    }

    private fun redrawMarkings() {
        log.debug("redrawing the markings")
        val x = 0

        val w = backgroundMarkings.width
        val h = backgroundMarkings.height
        val h2 = h / 2
        val p = 10.0
        val pw = w - 2 * p
        val ph = h - 2 * p
        val ph2 = ph / 2
        log.debug("Redrawing with x={} w={} h={}", x, w, h)

        backgroundMarkings.graphicsContext2D.apply {
            clearRect(0.0, 0.0, w, h)

            getTimeIntervals(w).let { (tOffset, pOffset) ->
                var dx = scrollBar.value
                var label = scrollBar.value
                textAlign = TextAlignment.CENTER

                log.debug("tOffset={} pOffset={}, x0={}, l0={}", tOffset, pOffset, dx, label)
                while (dx < pw) {
                    val text = "%.1fs".format(label)
                    log.trace("tick {}: {}", text, dx)
                    fillText(text, dx, h)
                    dx += pOffset
                    label += tOffset
                    strokeLine(dx, h - 10.0, dx, h - 20.0)
                    strokeLine(dx, 10.0, dx, 20.0)
                }
            }

            strokeLine(0.0, h2, w, h2)
        }
    }

    private fun getTimeIntervals(width: Double): Pair<Double, Double> {
        var minorTime = Math.pow(10.0, Math.floor(Math.log10(visibleTimeWindow)))
        var minorPixel = width * minorTime / visibleTimeWindow
        while (minorPixel > MAX_TICK_PIXEL_OFFSET) {
            minorPixel /= 10
            minorTime /= 10
        }
        return minorTime to minorPixel
    }

    companion object {
        const val MATCH_TIME_SECONDS = 150.0
        const val MAX_TICK_PIXEL_OFFSET = 200
        private val log = LoggerFactory.getLogger(TimelineController::class.java)
    }

}

data class TickIntervals(val minorTime: Double, val minorPixel: Double, val majorTime: Double, val majorPixel: Double)