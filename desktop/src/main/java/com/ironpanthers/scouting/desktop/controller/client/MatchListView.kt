package com.ironpanthers.scouting.desktop.controller.client

import com.ironpanthers.scouting.common.MutableCompetition
import com.ironpanthers.scouting.common.MutableMatch
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.scene.Parent
import javafx.scene.control.Label
import javafx.scene.control.TableView
import javafx.scene.layout.Priority
import org.slf4j.LoggerFactory
import tornadofx.*

class MatchListView : View() {

    override val root: Parent
    private lateinit var table: TableView<MatchModel>

    val competitionProperty = SimpleObjectProperty<MutableCompetition?>()
    var competition by competitionProperty

    private val displayedMatches = FXCollections.observableArrayList<MatchModel>()
    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        val searchBar = textfield {
            promptText = "Search by team..."
        }
        searchBar.textProperty().onChange { text ->
            val team = text!!
            val matches = competition?.matches?.filter { match ->
                match.red.any { it.team.toString().contains(team) } || match.blue.any { it.team.toString().contains(team) } || match.number.toString().contains(text)
            } ?: return@onChange
            displayedMatches.setAll(matches.map { MatchModel(it) })
        }

        root = borderpane {
            vgrow = Priority.ALWAYS
            hgrow = Priority.ALWAYS
            top = hbox {
                label("Matches")
                add(searchBar)
            }
            center {
                table = tableview {
                    column<MatchModel, Int>("#") {
                        it.value.number
                    }
                    column<MatchModel, Int>("Red 1") {
                        it.value.red[0]
                    }
                    column<MatchModel, Int>("Red 2") {
                        it.value.red[1]
                    }
                    column<MatchModel, Int>("Red 3") {
                        it.value.red[2]
                    }
                    column<MatchModel, Int>("Bed 1") {
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

        competitionProperty.onChange { comp ->
            searchBar.clear()
            if (comp != null) {
                displayedMatches.setAll(comp.matches.map { MatchModel(it) })
            } else {
                displayedMatches.clear()
            }

        }

        table.items.bind(displayedMatches) {it}

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