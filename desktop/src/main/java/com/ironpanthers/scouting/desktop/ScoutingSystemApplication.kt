package com.ironpanthers.scouting.desktop

import com.ironpanthers.scouting.common.GameDef2018
import javafx.application.Application
import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.Pane
import javafx.stage.Stage
import org.apache.log4j.PropertyConfigurator

class ScoutingSystemApplication : Application() {
    override fun start(primaryStage: Stage) {
        val file = javaClass.classLoader.getResource("scouting-view.fxml")
        val loader = FXMLLoader()
        loader.location = file
        val pane = loader.load<Pane>()
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
    val url = ScoutingSystemApplication::class.java.classLoader.getResourceAsStream("log4j.properties")
    PropertyConfigurator.configure("log4j.properties")
    Application.launch(ScoutingSystemApplication::class.java, *args)
}