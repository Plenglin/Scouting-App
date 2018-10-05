package com.ironpanthers.scouting.desktop

import com.ironpanthers.scouting.common.GameDef2018
import javafx.application.Application
import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage
import org.apache.log4j.BasicConfigurator

class ScoutingSystemApplication : Application() {
    override fun start(primaryStage: Stage) {
        val file = javaClass.classLoader.getResource("scouting-view.fxml")
        val loader = FXMLLoader()
        loader.location = file
        val pane = loader.load<AnchorPane>()
        val controller = loader.getController<ScoutingController>()
        val scene = Scene(pane, 1280.0, 720.0)
        controller.gameDef = GameDef2018

        primaryStage.scene = scene
        primaryStage.show()
        primaryStage.setOnCloseRequest {
            Platform.exit()
        }
    }
}

fun main(args: Array<String>) {
    BasicConfigurator.configure()
    Application.launch(ScoutingSystemApplication::class.java, *args)
}