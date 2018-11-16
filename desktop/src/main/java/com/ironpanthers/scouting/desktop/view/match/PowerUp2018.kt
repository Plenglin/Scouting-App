package com.ironpanthers.scouting.desktop.view.match

import com.ironpanthers.scouting.common.RobotEvent
import com.ironpanthers.scouting.frc2018.GameDef2018
import javafx.scene.Parent
import javafx.scene.control.Button
import javafx.scene.control.Label
import org.slf4j.LoggerFactory
import tornadofx.action
import tornadofx.anchorpane
import tornadofx.anchorpaneConstraints
import tornadofx.gridpane

class PowerUp2018 : EditorPanel() {
    override val root: Parent

    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        logger.debug("existing events: {}", GameDef2018.events)
        root = anchorpane {
            val cubeGrid = gridpane {
                anchorpaneConstraints {
                    topAnchor = 10.0
                    leftAnchor = 10.0
                }
            }
            cubeGrid.addRow(0, Label(), Label("vault"), Label("switch"), Label("scale"))
            listOf("hit", "miss", "pushed", "foul").forEachIndexed { i, type ->
                val children = listOf("vault", "switch", "scale").map { dest ->
                    val eventId = GameDef2018.createId("${type}_$dest")
                    if (GameDef2018.events.any { it.id == eventId }) {
                        logger.trace("creating button for {}", eventId)
                        Button().apply {
                            action {
                                val event = RobotEvent(eventId, System.currentTimeMillis())
                                logger.debug("sending event {}", event)
                                editorParent.onRobotEvent(event)
                            }
                        }
                    } else {
                        logger.trace("no event exists for {}", eventId)
                        Label()
                    }
                }
                cubeGrid.addRow(i + 1, Label(type), *children.toTypedArray())
            }
        }
    }

}