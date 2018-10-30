package com.ironpanthers.scouting.desktop.controller.server

import com.ironpanthers.scouting.common.CompetitionSummary
import com.ironpanthers.scouting.io.server.ServerEngine
import javafx.beans.property.*
import javafx.scene.control.SelectionMode
import javafx.scene.control.TableView
import javafx.scene.layout.BorderPane
import org.slf4j.LoggerFactory
import tornadofx.View
import tornadofx.*
import java.text.SimpleDateFormat

class CompetitionSelectionView : View() {

    override val root: BorderPane

    var result: CompetitionSummary? = null
    private val logger = LoggerFactory.getLogger(javaClass)
    private lateinit var listCompetitions: TableView<CompetitionSummary>

    init {
        root = borderpane {
            top {
                label("Select a competition...")
            }

            center {
                listCompetitions = tableview {
                    selectionModel.selectionMode = SelectionMode.SINGLE
                    column<CompetitionSummary, String>("Name") {
                        SimpleStringProperty(it.value.name)
                    }
                    val sdf = SimpleDateFormat("yyyy-MM-dd")
                    column<CompetitionSummary, String>("Date") {
                        SimpleStringProperty(sdf.format(it.value.date))
                    }
                    column<CompetitionSummary, Int>("Matches") {
                        SimpleObjectProperty<Int>(it.value.matchCount)
                    }
                    onUserSelect(2) {
                        result = it
                        modalStage?.close()
                    }
                }
            }
        }
        ServerEngine.dbBackend.listCompetitions { result ->
            logger.debug("Received {}", result)
            listCompetitions.items.apply {
                clear()
                addAll(result)
            }
        }
    }

}