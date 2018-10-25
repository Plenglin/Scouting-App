package com.ironpanthers.scouting.desktop.controller

import com.ironpanthers.scouting.common.MatchDescription
import javafx.beans.property.SimpleStringProperty
import javafx.fxml.FXML
import javafx.scene.control.TableColumn
import javafx.util.Callback

class MatchListViewController {

    @FXML lateinit var colMatchNumber: TableColumn<MatchDescription, String>
    @FXML lateinit var colRed1: TableColumn<MatchDescription, String>
    @FXML lateinit var colRed2: TableColumn<MatchDescription, String>
    @FXML lateinit var colRed3: TableColumn<MatchDescription, String>
    @FXML lateinit var colBlue1: TableColumn<MatchDescription, String>
    @FXML lateinit var colBlue2: TableColumn<MatchDescription, String>
    @FXML lateinit var colBlue3: TableColumn<MatchDescription, String>

    @FXML
    fun initialize() {
        colMatchNumber.cellValueFactory = Callback {
            SimpleStringProperty(it.value.number.toString())
        }
        colRed1.cellValueFactory = Callback {
            SimpleStringProperty(it.value.red.teams[0].team.toString())
        }
        colRed2.cellValueFactory = Callback {
            SimpleStringProperty(it.value.red.teams[1].team.toString())
        }
        colRed3.cellValueFactory = Callback {
            SimpleStringProperty(it.value.red.teams[2].team.toString())
        }
        colBlue1.cellValueFactory = Callback {
            SimpleStringProperty(it.value.blue.teams[0].team.toString())
        }
        colBlue2.cellValueFactory = Callback {
            SimpleStringProperty(it.value.blue.teams[1].team.toString())
        }
        colBlue3.cellValueFactory = Callback {
            SimpleStringProperty(it.value.blue.teams[2].team.toString())
        }
    }
}