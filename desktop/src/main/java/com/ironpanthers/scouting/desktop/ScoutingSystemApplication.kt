package com.ironpanthers.scouting.desktop

import com.ironpanthers.scouting.desktop.controller.Stages
import javafx.application.Application
import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import org.apache.log4j.PropertyConfigurator

class ScoutingSystemApplication : Application() {
    override fun start(primaryStage: Stage) {
        val cl = javaClass.classLoader
        val file = cl.getResource("views/main-menu-view.fxml")!!
        val pane = FXMLLoader.load<Parent>(file)
        val scene = Scene(pane)

        primaryStage.scene = scene

        Stages.mainMenu = primaryStage
        Stages.bluetoothDialog = Stage().apply {
            title = "Connect to Bluetooth Server..."
            val url = cl.getResource("views/bluetooth-dialog-view.fxml")!!
            val root = FXMLLoader.load<Parent>(file)
            setScene(Scene(root))
        }
        Stages.serverMonitor = Stage().apply {
            title = "Server Monitor"
            val url = cl.getResource("views/server-monitor-view.fxml")!!
            val root = FXMLLoader.load<Parent>(url)
            setScene(Scene(root))
        }
        Stages.competitionSelection = Stage().apply {
            title = "Select competition..."
            val url = cl.getResource("views/competition-selection-dialog.fxml")!!
            val root = FXMLLoader.load<Parent>(url)
            setScene(Scene(root))
        }

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