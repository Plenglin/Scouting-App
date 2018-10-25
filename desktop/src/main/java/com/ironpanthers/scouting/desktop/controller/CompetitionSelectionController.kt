package com.ironpanthers.scouting.desktop.controller

import com.ironpanthers.scouting.common.CompetitionDescription
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.SingleSelectionModel
import javafx.scene.control.TableView

class CompetitionSelectionController {

    @FXML
    private lateinit var listCompetitions: TableView<CompetitionDescription>
    @FXML
    private lateinit var btnSubmit: Button

    private lateinit var selectionModel: SingleSelectionModel<CompetitionDescription>

    @FXML
    fun initialize() {
        selectionModel = listCompetitions.selectionModel as SingleSelectionModel<CompetitionDescription>
        btnSubmit.disableProperty().bind(selectionModel.selectedIndexProperty().isEqualTo(-1))
    }

}