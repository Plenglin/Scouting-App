package com.ironpanthers.scouting.desktop.view

import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Priority
import tornadofx.*

class MainWindow : View() {

    override val root: Parent

    val chatView: ChatView = ChatView()
    val eventLogView = EventLogView()
    val connectionView = ConnectionView()

    init {

        root = borderpane {
            top = menubar {
                menu("File") {

                }
                menu("Edit") {

                }
            }

            left = toolbar {
                //alignment = Pos.TOP_LEFT
                orientation = Orientation.VERTICAL
                group {
                    togglebutton("Matches") {
                        rotate = -90.0
                    }
                }
            }

            right = toolbar {
                //alignment = Pos.TOP_RIGHT
                orientation = Orientation.VERTICAL
                group {
                    togglebutton("Connection") {
                        rotate = 90.0
                    }
                }
            }

            center = splitpane(Orientation.VERTICAL) {
                splitpane(Orientation.HORIZONTAL) {
                    hbox {
                        vgrow = Priority.ALWAYS
                        pane {

                        }
                    }

                    pane {

                    }

                    hbox {
                        vgrow = Priority.ALWAYS
                        pane {

                        }
                    }
                }

                vbox {
                    vgrow = Priority.ALWAYS
                    prefHeight = 0.0
                    splitpane(Orientation.HORIZONTAL) {
                        add(chatView)
                        add(eventLogView)
                    }
                }

            }

            bottom = toolbar {
                //alignment = Pos.BOTTOM_LEFT
                val btnChat = togglebutton("Chat")
                val btnEventLog = togglebutton("Event Log")

                chatView.root.visibleProperty().bind(btnChat.selectedProperty())
                eventLogView.root.visibleProperty().bind(btnEventLog.selectedProperty())
            }

        }
    }

}
