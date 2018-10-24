package com.ironpanthers.scouting.desktop.controller.frc2018

import com.ironpanthers.scouting.common.RobotEvent
import com.ironpanthers.scouting.desktop.controller.RobotEventPanelController
import com.ironpanthers.scouting.desktop.getAllNodes
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.layout.Pane
import org.slf4j.LoggerFactory

class PowerUpFXPanelController : RobotEventPanelController {
    private val log = LoggerFactory.getLogger(javaClass)

    override var onEventOccurred: (RobotEvent) -> Unit = {}
    override var teamNumber: Int = 0

    @FXML lateinit var root: Pane

    lateinit var buttons: List<Button>

    @FXML
    fun initialize() {
        buttons = getAllNodes(root).filter { it is Button }.map { it as Button }

        buttons.forEach { btn ->
            btn.setOnMouseClicked {
                log.debug("clicked on {}", btn)
                onEventOccurred(RobotEvent(btn.id, teamNumber))
            }
        }
    }

}