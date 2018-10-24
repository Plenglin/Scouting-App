package com.ironpanthers.scouting.desktop.controller

import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.input.MouseEvent
import javafx.stage.Stage

class MainMenuController {

    fun openServerDialog(mouseEvent: MouseEvent) {
        val resource = javaClass.classLoader.getResource("views/server-monitor-view.fxml")
        val root = FXMLLoader.load<Parent>(resource)
        val stage = Stage()
        stage.title = "Server Monitor"
        stage.scene = Scene(root, 450.0, 300.0)
        stage.show()
    }

}