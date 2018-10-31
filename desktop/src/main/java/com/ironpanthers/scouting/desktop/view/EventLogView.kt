package com.ironpanthers.scouting.desktop.view

import javafx.collections.FXCollections
import javafx.scene.Parent
import javafx.scene.layout.Priority
import org.slf4j.LoggerFactory
import tornadofx.*
import java.text.SimpleDateFormat
import java.util.*

class EventLogView : View() {
    override val root: Parent

    private val events = FXCollections.observableArrayList<Event>()
    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        root = anchorpane {
            listview<Event>(events) {
                anchorpaneConstraints {
                    topAnchor = 0.0
                    bottomAnchor = 0.0
                    leftAnchor = 0.0
                    rightAnchor = 0.0
                }

                hgrow = Priority.ALWAYS
                vgrow = Priority.ALWAYS

                onUserSelect {
                    logger.debug("user selected $it")
                    it.onClick()
                }

            }
        }
    }

    fun appendMessage(msg: String, onClick: () -> Unit = {}) {
        events.add(Event(msg, onClick))
    }

}

private val format = SimpleDateFormat("HH:mm:ss")

private data class Event(val msg: String, val onClick: () -> Unit) {
    val time = Date()

    override fun toString(): String {
        return "${format.format(time)} - $msg"
    }
}