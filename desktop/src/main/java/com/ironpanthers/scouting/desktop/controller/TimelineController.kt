package com.ironpanthers.scouting.desktop.controller

import com.ironpanthers.scouting.desktop.lerp
import javafx.beans.property.SimpleDoubleProperty
import javafx.fxml.FXML
import javafx.scene.canvas.Canvas
import javafx.scene.control.ScrollBar
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
        scrollBar.maxProperty().set(1.0)
        scrollBar.visibleAmountProperty().bind(visibleWindowProperty.divide(MATCH_TIME_SECONDS))

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
        val x = 0

        val w = backgroundMarkings.width
        val h = backgroundMarkings.height
        val h2 = h / 2
        val pw = w - 2 * TIMELINE_PADDING
        val ph = h - 2 * TIMELINE_PADDING

        val px0 = TIMELINE_PADDING
        val px1 = px0 + pw

        val ph2 = ph / 2
        log.debug("Redrawing with x={} w={} h={}", x, w, h)

        backgroundMarkings.graphicsContext2D.apply {
            clearRect(0.0, 0.0, w, h)

            getTimeIntervals(w).let { (dt, dp) ->
                val t0 = scrollBar.value * (MATCH_TIME_SECONDS - visibleTimeWindow)
                val t1 = t0 + visibleTimeWindow

                textAlign = TextAlignment.CENTER

                if (dp > MAX_USE_MAJOR_PIXEL_OFFSET) {
                    var label = Math.floor(scrollBar.value / dt) * dt  // round to lowest time increment
                    var dx = label.lerp(t0, t1, px0, px1)  // initial position
                    log.debug("Not using major/minor pixels: t0={} t1={} dt={} dp={} x0={} l0={}", t0, t1, dt, dp, dx, label)

                    while (dx < pw) {
                        val text = "%.1fs".format(label)
                        val height = 15.0
                        log.trace("tick {}: {}", text, dx)
                        fillText(text, dx, h)
                        dx += dp
                        label += dt
                        strokeLine(dx, h - 10.0, dx, h - 10 - height)
                        strokeLine(dx, 10.0, dx, 10 + height)
                    }
                } else {
                    val dtm = dt * TICK_TIME_DIVISIONS
                    var label = Math.floor(scrollBar.value / dtm) * dtm  // round to lowest time increment
                    var dx = label.lerp(t0, t1, px0, px1)  // initial position

                    log.debug("Will use major/minor pixels: t0={} t1={} dt={} dp={} x0={} l0={}", t0, t1, dt, dp, dx, label)
                    var i = 1
                    while (dx < pw) {
                        var height = 10.0

                        if (i == 1) {
                            val text = "%.1fs".format(label)
                            log.trace("tick {}: {}", text, dx)
                            fillText(text, dx, h)
                            height = 15.0
                        }
                        strokeLine(dx, h - 10.0, dx, h - 10 - height)
                        strokeLine(dx, 10.0, dx, 10 + height)
                        i = if (i == TICK_TIME_DIVISIONS) 1 else i + 1
                        dx += dp
                        label += dt
                    }
                }
            }

            strokeLine(0.0, h2, w, h2)
        }
    }

    private fun getTimeIntervals(width: Double): Pair<Double, Double> {
        var minorTime = Math.pow(TICK_TIME_DIVISIONS.toDouble(), Math.floor(Math.log(visibleTimeWindow) / Math.log(TICK_TIME_DIVISIONS.toDouble())))
        var minorPixel = width * minorTime / visibleTimeWindow
        while (minorPixel > MAX_MINOR_PIXEL_OFFSET) {
            minorPixel /= TICK_TIME_DIVISIONS
            minorTime /= TICK_TIME_DIVISIONS
        }
        return minorTime to minorPixel
    }

    companion object {
        const val MATCH_TIME_SECONDS = 150.0
        /**
         * Ticks will always be smaller than this
         */
        const val MAX_MINOR_PIXEL_OFFSET = 250
        /**
         * Major/minor ticks will be used if minor ticks are smaller than this
         */
        const val MAX_USE_MAJOR_PIXEL_OFFSET = 100
        const val TIMELINE_PADDING = 20.0

        /**
         * What to split the ticks into
         */
        const val TICK_TIME_DIVISIONS = 5

        private val log = LoggerFactory.getLogger(TimelineController::class.java)
    }

}

data class TickIntervals(val minorTime: Double, val minorPixel: Double, val majorTime: Double, val majorPixel: Double)