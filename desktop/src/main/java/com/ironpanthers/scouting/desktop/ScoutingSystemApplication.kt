package com.ironpanthers.scouting.desktop

import com.ironpanthers.scouting.desktop.controller.ScoutingController
import com.ironpanthers.scouting.frc2018.GameDef2018
import javafx.application.Application
import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.Pane
import javafx.stage.Stage
import org.apache.log4j.PropertyConfigurator

class ScoutingSystemApplication : Application() {
    override fun start(primaryStage: Stage) {
        val file = javaClass.classLoader.getResource("views/scouting-view.fxml")!!
        val loader = FXMLLoader()
        loader.location = file
        val pane = loader.load<Pane>()
        val controller = loader.getController<ScoutingController>()
        val scene = Scene(pane, 1280.0, 720.0)
        controller.gameDef = GameDef2018

        primaryStage.scene = scene
        /*primaryStage.isResizable = false
        primaryStage.width = 800.0
        primaryStage.height = 600.0*/
        primaryStage.show()
        primaryStage.setOnCloseRequest {
            Platform.exit()
        }
    }
}

fun main(args: Array<String>) {
    PropertyConfigurator.configure("log4j.properties")
    Application.launch(ScoutingSystemApplication::class.java, *args)
}