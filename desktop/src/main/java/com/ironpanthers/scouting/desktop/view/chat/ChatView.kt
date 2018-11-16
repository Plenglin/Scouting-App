package com.ironpanthers.scouting.desktop.view.chat

import javafx.scene.Parent
import javafx.scene.layout.Priority
import tornadofx.*

class ChatView : View() {
    override val root: Parent

    init {
        root = borderpane {
            top = label("Chat")
            center = textarea {
                isEditable = false
            }
            bottom = hbox {
                hgrow = Priority.ALWAYS
                textfield {
                    isEditable = true
                }
                button("Send")
            }
        }
    }
}