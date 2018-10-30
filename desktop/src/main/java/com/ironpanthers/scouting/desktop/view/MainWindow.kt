package com.ironpanthers.scouting.desktop.view

import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.layout.Priority
import tornadofx.*

class MainWindow : View() {

    override val root: Parent

    init {
        root = borderpane {
            top = menubar {
                menu("File") {

                }
                menu("Edit") {

                }
            }

            left = hbox {
                toolbar {
                    alignment = Pos.TOP_LEFT
                    group {
                        button("Matches") {
                            rotate = -90.0
                        }
                    }
                }
                pane {

                }
            }

            right = hbox {
                pane {

                }
                toolbar {
                    alignment = Pos.TOP_RIGHT
                    group {
                        button("Server") {
                            rotate = -90.0
                        }
                    }
                }
            }

            center {
                pane {

                }
            }

            bottom = vbox {
                hbox {
                    vbox {
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

                    textarea {
                        isEditable = false
                    }
                }
                toolbar {
                    alignment = Pos.BOTTOM_LEFT
                    button("Chat")
                    button("Event Log")
                }
            }
        }
    }

}
