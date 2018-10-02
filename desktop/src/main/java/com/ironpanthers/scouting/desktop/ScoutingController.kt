package com.ironpanthers.scouting.desktop

import com.ironpanthers.scouting.common.GameDef
import javafx.fxml.FXML
import javafx.scene.canvas.Canvas
import javafx.scene.control.Button
import javafx.scene.input.KeyEvent
import javafx.scene.layout.FlowPane
import javafx.scene.layout.Pane
import org.slf4j.LoggerFactory
import java.util.*

class ScoutingController {

    private val logger = LoggerFactory.getLogger(javaClass)
    private val timer = Timer()

    @FXML
    lateinit var root: Pane

    @FXML
    lateinit var targetButtons: FlowPane

    @FXML
    lateinit var robotTimeline: Canvas

    var gameDef: GameDef? = null
        set(value) {
            logger.info("setting GameDef to {}", value)
            field = value

            targetButtons.children.apply {
                clear()
                setAll(value?.events?.map { eventDef ->
                    val btn = eventDef.createButton()
                    btn.setOnMouseClicked {
                        logger.debug("clicked {}", eventDef)
                    }

                    btn
                })
            }
        }

    @FXML
    fun initialize() {
        logger.info("Initializing")
        targetButtons.children.addAll(Button("test1"), Button("test2"))
    }

    @FXML
    fun shutdown() {
        logger.info("Stopping")
    }

    fun handleOnKeyPressed(keyEvent: KeyEvent) {
        logger.debug("key event: {}", keyEvent)
        gameDef?.apply {
            events.find { it.listener(keyEvent) }?.let {
                logger.debug("selected: {}", it)
            }
        }
    }

}