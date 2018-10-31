package com.ironpanthers.scouting.desktop.controller.client

import com.ironpanthers.scouting.common.MutableCompetition
import com.ironpanthers.scouting.common.MutableMatch
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.Parent
import javafx.scene.control.TableView
import tornadofx.*

class MatchListView : View() {

    override val root: Parent
    private lateinit var table: TableView<MatchModel>

    val competitionProperty = SimpleObjectProperty<MutableCompetition?>()
    var competition by competitionProperty

    init {
        root = borderpane {
            center {
                table = tableview {
                    column<MatchModel, Int>("#") {
                        it.value.number
                    }
                    column<MatchModel, Int>("R1") {
                        it.value.red[0]
                    }
                    column<MatchModel, Int>("R2") {
                        it.value.red[1]
                    }
                    column<MatchModel, Int>("R3") {
                        it.value.red[2]
                    }
                    column<MatchModel, Int>("B1") {
                        it.value.blue[0]
                    }
                    column<MatchModel, Int>("B2") {
                        it.value.blue[1]
                    }
                    column<MatchModel, Int>("B3") {
                        it.value.blue[0]
                    }
                }
            }
        }

        competitionProperty.onChange {
            if (it != null) {
                table.items.setAll(it.matches.map { MatchModel(it) })
            } else {
                table.items.clear()
            }

        }

    }
}

private class MatchModel(data: MutableMatch) {

    var data: MutableMatch = data
        set(value) {
            field = value
            blue.zip(data.blue).forEach { (a, b) -> a.value = b.team }
            red.zip(data.red).forEach { (a, b) -> a.value = b.team }
            number.value = value.number
        }

    val blue: List<SimpleObjectProperty<Int>> = data.blue.map { SimpleObjectProperty<Int>(it.team) }
    val red: List<SimpleObjectProperty<Int>> = data.red.map { SimpleObjectProperty<Int>(it.team) }
    val number: SimpleObjectProperty<Int> = SimpleObjectProperty<Int>(data.number)
}