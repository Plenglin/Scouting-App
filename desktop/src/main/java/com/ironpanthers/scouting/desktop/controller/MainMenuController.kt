package com.ironpanthers.scouting.desktop.controller

import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import org.slf4j.LoggerFactory

class MainMenuController {

    private val log = LoggerFactory.getLogger(javaClass)

    fun showServerDialog() {
        log.info("Opening server dialog")
        Stages.serverMonitor.show()
    }

    fun startLocalClient() {
        log.info("Opening local client")

    }

    fun showBluetoothDialog() {
        log.info("Opening bluetooth dialog")
        log.warn("Bluetooth not yet implemented!")
    }

    fun showTCPDialog() {
        log.info("Opening TCP dialog")
        log.warn("TCP not yet implemented!")
    }

}