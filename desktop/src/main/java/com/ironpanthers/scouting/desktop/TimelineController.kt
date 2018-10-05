package com.ironpanthers.scouting.desktop

import javafx.fxml.FXML
import javafx.geometry.Orientation
import javafx.scene.canvas.Canvas
import javafx.scene.control.ScrollBar
import javafx.scene.control.ScrollPane
import javafx.scene.layout.Pane
import javafx.scene.text.TextAlignment
import org.slf4j.LoggerFactory

class TimelineController {

    private val log = LoggerFactory.getLogger(javaClass)

    @FXML lateinit var backgroundMarkings: Canvas
    //@FXML lateinit var labelLayer: Pane
    //@FXML lateinit var scrollPane: ScrollPane

    private var zoom = 1.0

    @FXML
    fun initialize() {
        /*scrollPane.setOnScroll {
            if (it.isControlDown) {
                val delta = Math.signum(it.deltaY) * 1.5
                log.debug("zooming by {}", delta)
                zoom *= delta
                labelLayer.prefWidth *= delta
                redrawMarkings()
            }
        }*/
        redrawMarkings()
    }

    private fun redrawMarkings() {
        backgroundMarkings.apply {
            /*val scrollBar = scrollPane.lookupAll(".scroll-bar")
                    .asSequence()
                    .map { it as ScrollBar }
                    .find { it.orientation == Orientation.HORIZONTAL }
            val x = scrollPane.hmin*/
            val x = 0

            val w = width
            val h = height
            val h2 = h / 2
            val p = 10
            val pw = w - 2 * p
            val ph = h - 2 * p
            val ph2 = ph / 2
            log.debug("Redrawing with x={} w={} h={}", x, w, h)

            val g = graphicsContext2D
            g.clearRect(0.0, 0.0, w, h)

            getTimeIntervals(pw).let { (tOffset, pOffset) ->
                log.debug("tOffset={} pOffset={}", tOffset, pOffset)
                var dx = p.toDouble()
                var label = 0
                g.textAlign = TextAlignment.CENTER
                while (dx < pw) {
                    g.fillText("${label}s", dx, h)
                    dx += pOffset
                    label += tOffset
                    g.strokeLine(dx, h - 10.0, dx, h - 20.0)
                    g.strokeLine(dx, 10.0, dx, 20.0)
                }
            }

            g.strokeLine(0.0, h2, w, h2)
        }
    }

    private fun getTimeIntervals(width: Double): Pair<Int, Double> {
        var pixelOffset = width
        var timeOffset = MATCH_TIME_SECONDS
        val divide = MATCH_TIME_SECONDS / TIME_INTERVAL_ROUNDING
        while (timeOffset > TIME_INTERVAL_ROUNDING && pixelOffset > MAX_TIME_INTERVAL_PIXEL_OFFSET) {
            pixelOffset /= divide
            timeOffset /= divide
        }
        return timeOffset.coerceAtLeast(1) to pixelOffset
    }

    companion object {
        const val MATCH_TIME_SECONDS = 150
        const val MAX_TIME_INTERVAL_PIXEL_OFFSET = 200
        const val TIME_INTERVAL_ROUNDING = 5
    }

}