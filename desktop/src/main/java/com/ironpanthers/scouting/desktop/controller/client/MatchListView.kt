package com.ironpanthers.scouting.desktop.controller.client

import com.ironpanthers.scouting.common.MatchSummary
import com.ironpanthers.scouting.common.MatchDescription
import com.ironpanthers.scouting.io.client.ClientStrategy
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.Parent
import javafx.scene.control.TableView
import tornadofx.*

class MatchListView(private val client: ClientStrategy) : View() {

    override val root: Parent
    private lateinit var table: TableView<MatchSummary>

    init {
        root = borderpane {
            center {
                table = tableview {
                    column<MatchSummary, Int>("#") {
                        SimpleObjectProperty<Int>(it.value.number)
                    }
                    column<MatchSummary, Int>("R1") {
                        SimpleObjectProperty<Int>(it.value.red[0])
                    }
                    column<MatchSummary, Int>("R2") {
                        SimpleObjectProperty<Int>(it.value.red[1])
                    }
                    column<MatchSummary, Int>("R3") {
                        SimpleObjectProperty<Int>(it.value.red[2])
                    }
                    column<MatchSummary, Int>("B1") {
                        SimpleObjectProperty<Int>(it.value.blue[0])
                    }
                    column<MatchSummary, Int>("B2") {
                        SimpleObjectProperty<Int>(it.value.blue[1])
                    }
                    column<MatchSummary, Int>("B3") {
                        SimpleObjectProperty<Int>(it.value.blue[2])
                    }
                }
            }
        }

        client.getCompetitionDescription {
            table.items.addAll(it.matches)
        }

    }
}