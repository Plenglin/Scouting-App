package com.ironpanthers.scouting.desktop

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage

class ScoutingSystemApplication : Application() {
    override fun start(primaryStage: Stage) {
        val file = javaClass.classLoader.getResource("ScoutingView.fxml")
        val pane = FXMLLoader.load<AnchorPane>(file)

        primaryStage.scene = Scene(pane, 300.0, 300.0)
        primaryStage.show()
    }
}

fun main(args: Array<String>) {
    Application.launch(ScoutingSystemApplication::class.java, *args)
}