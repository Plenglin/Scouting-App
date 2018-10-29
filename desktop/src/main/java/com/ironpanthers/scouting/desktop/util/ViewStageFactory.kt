package com.ironpanthers.scouting.desktop.util

import com.ironpanthers.scouting.desktop.controller.ServerMonitorView
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

interface ControllerWithStage {
    fun acceptStage(stage: Stage)
}

abstract class ViewStageFactory<C : ControllerWithStage>(val viewUrl: String) {
    fun create(): Pair<Stage, C> {
        val cl = javaClass.classLoader
        val loader = FXMLLoader()

        loader.location = cl.getResource(viewUrl)!!

        val root = loader.load<Parent>()
        val controller = loader.getController<ServerMonitorView>() as C

        val stage = Stage().apply {
            title = "Server Monitor"
            scene = Scene(root)
        }

        controller.acceptStage(stage)

        return stage to controller
    }
}