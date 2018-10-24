package com.ironpanthers.scouting.desktop.controller

import com.ironpanthers.scouting.desktop.lerp
import javafx.beans.property.SimpleDoubleProperty
import javafx.fxml.FXML
import javafx.scene.canvas.Canvas
import javafx.scene.control.ScrollBar
import javafx.scene.layout.Pane
import javafx.scene.text.TextAlignment
import org.slf4j.LoggerFactory
import tornadofx.*

class TimelineController {

    @FXML lateinit var root: Pane
    @FXML lateinit var backgroundCanvas: Canvas
    @FXML lateinit var labelLayer: Pane
    @FXML lateinit var scrollBar: ScrollBar  // It will have the value of t0

    private val visibleWindowProperty = SimpleDoubleProperty(ACTUAL_TIMELINE_LENGTH)

    private var visibleTimeWindow by visibleWindowProperty

    @FXML
    fun initialize() {
        scrollBar.minProperty().set(-TIMELINE_TEMPORAL_PADDING / MATCH_TIME_SECONDS)
        scrollBar.maxProperty().set(ACTUAL_TIMELINE_LENGTH / MATCH_TIME_SECONDS)
        scrollBar.visibleAmountProperty()
                .bind((visibleWindowProperty / ACTUAL_TIMELINE_LENGTH) * (scrollBar.maxProperty() - scrollBar.minProperty()))

        scrollBar.valueProperty().onChange {
            log.debug("dragged scrollbar to t0={}", it)
            redrawMarkings()
        }
        backgroundCanvas.setOnScroll {
            if (it.isControlDown && !it.isAltDown && !it.isShiftDown) {
                val delta = Math.exp(Math.signum(it.deltaY) * -ZOOM_FACTOR)
                visibleTimeWindow = (visibleTimeWindow * delta).coerceIn(MIN_TIME_WINDOW, ACTUAL_TIMELINE_LENGTH)
                log.debug("zooming to {}", visibleTimeWindow)
                labelLayer.prefWidth *= delta
                redrawMarkings()
            }
        }

        root.widthProperty().onChange {
            log.debug("resized: w={}", it)
            backgroundCanvas.width = it
            redrawMarkings()
        }

        root.heightProperty().onChange {
            log.debug("resized: h={}", it)
            backgroundCanvas.height = it - scrollBar.height
            redrawMarkings()
        }

        redrawMarkings()
    }

    private fun redrawMarkings() {
        val x = 0

        val w = backgroundCanvas.width
        val h = backgroundCanvas.height
        val h2 = h / 2

        log.debug("Redrawing with x={} w={} h={}", x, w, h)

        backgroundCanvas.graphicsContext2D.apply {
            clearRect(0.0, 0.0, w, h)

            getTimeIntervals(w).let { (dt, dp) ->
                val t0 = scrollBar.value * (ACTUAL_TIMELINE_LENGTH - visibleTimeWindow)
                val t1 = t0 + visibleTimeWindow

                textAlign = TextAlignment.CENTER

                if (dp > MAX_USE_MAJOR_PIXEL_OFFSET) {
                    var label = Math.floor(scrollBar.value / dt) * dt  // round to lowest time increment
                    var dx = label.lerp(t0, t1, 0.0, w)  // initial position
                    log.debug("Not using major/minor pixels: t0={} t1={} x0={} l0={} dt={} dp={}", t0, t1, dt, dp, dx, label)

                    while (dx < w) {
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
                    var dx = label.lerp(t0, t1, 0.0, w)  // initial position

                    log.debug("Will use major/minor pixels: t0={} t1={} x0={} l0={} dt={} dp={}", t0, t1, dt, dp, dx, label)
                    var i = 1
                    while (dx < w) {
                        i = if (i == TICK_TIME_DIVISIONS) 1 else i + 1
                        dx += dp
                        label += dt
                        var height = 10.0

                        if (i == 1) {
                            val text = "%.1fs".format(label)
                            log.trace("tick {}: {}", text, dx)
                            fillText(text, dx, h)
                            height = 15.0
                        }
                        strokeLine(dx, h - 10.0, dx, h - 10 - height)
                        strokeLine(dx, 10.0, dx, 10 + height)
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
        const val ZOOM_FACTOR = 0.2
        const val MATCH_TIME_SECONDS = 150.0
        /**
         * Ticks will always be smaller than this
         */
        const val MAX_MINOR_PIXEL_OFFSET = 250
        /**
         * Major/minor ticks will be used if minor ticks are smaller than this
         */
        const val MAX_USE_MAJOR_PIXEL_OFFSET = 100

        /**
         * Left and right time padding for the timeline, so you can see 0s and [MATCH_TIME_SECONDS]s markings
         */
        const val TIMELINE_TEMPORAL_PADDING = 2.0

        /**
         * How long the timeline is including padding
         */
        const val ACTUAL_TIMELINE_LENGTH = MATCH_TIME_SECONDS + 2 * TIMELINE_TEMPORAL_PADDING

        /**
         * What to split the ticks into
         */
        const val TICK_TIME_DIVISIONS = 5

        /**
         * The time window will never be less than this
         */
        const val MIN_TIME_WINDOW = 1.0

        private val log = LoggerFactory.getLogger(TimelineController::class.java)
    }

}
