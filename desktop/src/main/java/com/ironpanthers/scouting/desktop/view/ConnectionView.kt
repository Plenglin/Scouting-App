package com.ironpanthers.scouting.desktop.view

import javafx.scene.Parent
import tornadofx.View
import tornadofx.button
import tornadofx.toolbar
import tornadofx.vbox

class ConnectionView : View() {

    override val root: Parent

    init {
        root = vbox {
            toolbar {
                button("Add server")
                button("Refresh")
            }
        }
    }

}