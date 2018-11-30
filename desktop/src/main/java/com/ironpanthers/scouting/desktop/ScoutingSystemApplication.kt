package com.ironpanthers.scouting.desktop

import com.ironpanthers.scouting.desktop.util.ioExecutor
import com.ironpanthers.scouting.desktop.view.MainWindow
import javafx.application.Application
import javafx.application.Platform
import javafx.beans.property.SimpleStringProperty
import javafx.stage.Stage
import org.apache.log4j.PropertyConfigurator
import tornadofx.App

class ScoutingSystemApplication : App(MainWindow::class) {
    override fun start(stage: Stage) {
        super.start(stage)
        stage.setOnCloseRequest {
            Platform.exit()
            ioExecutor.shutdown()
        }
        val tag = System.getProperty("com.ironpanthers.scouting.desktop.tag")
        val title = "Panther Scouting System ${if (tag != null) "[$tag]" else ""}"
        stage.titleProperty().bind(SimpleStringProperty(title))
    }
}

fun main(args: Array<String>) {
    PropertyConfigurator.configure("log4j.properties")
    //ServerEngine.dbBackend = SQLiteBackend("jdbc:sqlite:data.sqlite3")
    Application.launch(ScoutingSystemApplication::class.java, *args)
}