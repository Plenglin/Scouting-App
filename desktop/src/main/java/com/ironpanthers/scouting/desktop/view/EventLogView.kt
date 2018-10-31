package com.ironpanthers.scouting.desktop.view

import javafx.scene.Parent
import javafx.scene.layout.Priority
import tornadofx.*

class EventLogView : View() {
    override val root: Parent

    init {
        root = anchorpane {
            textarea {
                anchorpaneConstraints {
                    topAnchor = 0.0
                    bottomAnchor = 0.0
                    leftAnchor = 0.0
                    rightAnchor = 0.0
                }

                hgrow = Priority.ALWAYS
                vgrow = Priority.ALWAYS


                isEditable = false
            }
        }
    }
}