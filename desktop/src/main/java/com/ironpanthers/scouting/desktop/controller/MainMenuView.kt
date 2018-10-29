package com.ironpanthers.scouting.desktop.controller

import javafx.scene.layout.BorderPane
import org.slf4j.LoggerFactory
import tornadofx.*

class MainMenuView : View() {

    override val root: BorderPane
    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        title = "5026 Scouting System"
        root = borderpane {
            top {
                label("Iron Panthers Scouting System")
            }
            center = vbox {
                button("Server") {
                    action {
                        logger.info("Opening server dialog")
                        find<ServerMonitorView>().openWindow(block = true)
                        logger.info("Server dialog closed")
                    }
                }
                button("Connect to Local") {
                    action {
                        logger.info("Opening local client")
                    }
                }
                button("Connect to Bluetooth") {
                    action {
                        logger.info("Opening bluetooth dialog")
                        logger.warn("Bluetooth not yet implemented!")
                    }
                }
                button("Connect via TCP") {
                    action {
                        logger.info("Opening TCP dialog")
                        logger.warn("TCP not yet implemented!")
                    }
                }
            }
        }
    }
}