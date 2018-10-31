package com.ironpanthers.scouting.desktop.view

import javafx.scene.Parent
import javafx.scene.layout.Priority
import tornadofx.*

class ChatView : View() {
    override val root: Parent

    init {
        root = vbox {
            hgrow = Priority.ALWAYS
            vgrow = Priority.ALWAYS
            textarea {
                isEditable = false
            }
            hbox {
                hgrow = Priority.ALWAYS
                textfield {
                    isEditable = true
                }
                button("Send")
            }
        }
    }
}