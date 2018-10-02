package com.ironpanthers.scouting.desktop

import com.ironpanthers.scouting.common.GameDef
import javafx.fxml.FXML
import javafx.scene.canvas.Canvas
import javafx.scene.control.Button
import javafx.scene.input.KeyEvent
import javafx.scene.layout.FlowPane
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import org.slf4j.LoggerFactory
import java.util.*

class ScoutingController {

    private val logger = LoggerFactory.getLogger(javaClass)
    private val timer = Timer()

    @FXML private lateinit var root: Pane
    @FXML private lateinit var targetButtons: FlowPane
    @FXML private lateinit var robotTimeline: Canvas
    @FXML private lateinit var endgameStates: VBox

    var gameDef: GameDef? = null
        set(value) {
            logger.info("setting GameDef to {}", value)
            field = value

            targetButtons.children.apply {
                clear()
                setAll(value?.events?.map { eventDef ->
                    val btn = eventDef.createButton()
                    btn.setOnMouseClicked {
                        logger.debug("triggered event: {}", eventDef)
                    }

                    btn
                })
            }

            endgameStates.children.apply {
                clear()
                setAll(value?.endStates?.map { endState ->
                    val btn = endState.createButton()
                    btn.setOnMouseClicked {
                        logger.debug("set end state: {}", endState)
                    }

                    btn
                })
            }

        }

    @FXML
    fun initialize() {
        logger.info("Initializing")
    }

    @FXML
    fun shutdown() {
        logger.info("Stopping")
    }

    fun handleOnKeyPressed(keyEvent: KeyEvent) {
        logger.debug("key event: {}", keyEvent)
        gameDef?.apply {
            events.find { it.keyCombo.test(keyEvent) }?.let {
                logger.debug("selected: {}", it)
            }
        }
    }

}