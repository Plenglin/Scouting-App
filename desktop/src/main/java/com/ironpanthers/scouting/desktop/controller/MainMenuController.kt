package com.ironpanthers.scouting.desktop.controller

import javafx.scene.layout.BorderPane
import org.slf4j.LoggerFactory
import tornadofx.*

class MainMenuController : View() {

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
                    logger.info("Opening server dialog")
                }
                button("Connect to Local") {
                    logger.info("Opening local client")
                }
                button("Connect to Bluetooth") {
                    logger.info("Opening bluetooth dialog")
                    logger.warn("Bluetooth not yet implemented!")
                }
                button("Connect via TCP") {
                    logger.info("Opening TCP dialog")
                    logger.warn("TCP not yet implemented!")
                }
            }
        }
    }
}