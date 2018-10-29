package com.ironpanthers.scouting.desktop.controller

import com.ironpanthers.scouting.common.CompetitionDescription
import javafx.beans.property.ListProperty
import javafx.beans.property.SimpleListProperty
import javafx.collections.ObservableList
import javafx.scene.control.SelectionMode
import javafx.scene.control.TableView
import javafx.scene.layout.BorderPane
import tornadofx.Fragment
import tornadofx.View
import tornadofx.*

class CompetitionSelectionView : Fragment() {

    override val root: BorderPane

    private lateinit var listCompetitions: TableView<CompetitionDescription>

    private val list = SimpleListProperty<CompetitionDescription>()

    init {
        root = borderpane {
            listCompetitions = tableview(list) {
                selectionModel.selectionMode = SelectionMode.SINGLE
            }
        }
    }

    override fun onDock() {
        root.prefWidth = 600.0
        root.prefHeight = 400.0
    }

}