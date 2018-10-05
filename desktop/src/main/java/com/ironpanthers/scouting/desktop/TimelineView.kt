package com.ironpanthers.scouting.desktop

import javafx.event.EventHandler
import javafx.scene.canvas.Canvas
import javafx.scene.text.TextAlignment
import org.slf4j.LoggerFactory

class TimelineView : Canvas() {

    private val log = LoggerFactory.getLogger(javaClass)
    private val originalWidth: Double

    init {
        originalWidth = width
        setOnScroll {
            log.debug("scroll")
            if (it.isControlDown) {
                val delta = it.deltaY * 10
                log.debug("zooming by {}", delta)
                width += delta
            }
        }

        onMouseEntered = EventHandler {
            
        }

        widthProperty().addListener { _ -> redraw() }
        redraw()
    }

    private fun redraw() {
        val w = width
        val h = height
        val h2 = h / 2
        val p = 10
        val pw = w - p
        val ph = h - p
        val ph2 = ph / 2
        log.debug("Redrawing with w={} h={}", w, h)

        val g = graphicsContext2D
        g.clearRect(0.0, 0.0, w, h)

        getTimeIntervals(pw).let { (tOffset, pOffset) ->
            log.debug("tOffset={} pOffset={}", tOffset, pOffset)
            var x = p.toDouble()
            var t = 0
            g.textAlign = TextAlignment.CENTER
            while (x < pw) {
                g.fillText("${t}s", x, h)
                x += pOffset
                t += tOffset
                g.strokeLine(x, h - 10.0, x, h - 20.0)
                g.strokeLine(x, 10.0, x, 20.0)
            }
        }

        g.strokeLine(0.0, h2, w, h2)

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