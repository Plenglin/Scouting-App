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
        root = vbox {
            toolbar {
                button("Clear") {
                    action {
                        events.clear()
                    }
                }
            }

            listview<Event>(events) {

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