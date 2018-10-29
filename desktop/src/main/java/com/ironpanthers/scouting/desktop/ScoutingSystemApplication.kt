package com.ironpanthers.scouting.desktop

import com.ironpanthers.scouting.desktop.controller.MainMenuView
import com.ironpanthers.scouting.desktop.io.server.SQLiteBackend
import com.ironpanthers.scouting.desktop.util.ioExecutor
import com.ironpanthers.scouting.io.server.ServerEngine
import javafx.application.Application
import javafx.application.Platform
import javafx.stage.Stage
import org.apache.log4j.PropertyConfigurator
import tornadofx.App

class ScoutingSystemApplication : App(MainMenuView::class) {

    override fun start(stage: Stage) {
        super.start(stage)
        stage.setOnCloseRequest {
            Platform.exit()
            ioExecutor.shutdown()
        }
    }
    /*override fun start(primaryStage: Stage) {
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
        Stages.serverMonitor = ServerMonitorView.create().first

        primaryStage.show()
        primaryStage.setOnCloseRequest {
            Platform.exit()
        }
    }*/
}

fun main(args: Array<String>) {
    PropertyConfigurator.configure("log4j.properties")
    ServerEngine.dbBackend = SQLiteBackend("jdbc:sqlite:data.sqlite3")
    Application.launch(ScoutingSystemApplication::class.java, *args)
}