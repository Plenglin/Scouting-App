package com.ironpanthers.scouting.desktop.controller

import com.ironpanthers.scouting.common.CompetitionDescription
import javafx.scene.control.RadioButton
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane
import tornadofx.*
import java.time.LocalDate

class CompetitionCreationWizard : View() {
    override val root: BorderPane

    private val creationToggleGroup = ToggleGroup()

    lateinit var radioCreate: RadioButton
    lateinit var radioImport: RadioButton

    lateinit var paneCreation: Pane
    lateinit var paneImportTBA: Pane

    var result: CompetitionDescription? = null

    init {
        root = borderpane {
            left = vbox {
                label("Select an initialization method...")
                radioCreate = radiobutton {
                    toggleGroup = creationToggleGroup
                    text = "Manually Create"
                }
                radioImport = radiobutton {
                    toggleGroup = creationToggleGroup
                    text = "Import from TheBlueAlliance"
                }
            }
            center = stackpane {
                vbox {
                    visibleWhen { radioCreate.selectedProperty() }

                    gridpane {
                        row {
                            label("Name")
                            textfield()
                        }
                        row {
                            label("Number of Matches")
                            spinner(editable = true, min = 1)
                        }
                        row {
                            label("Date")
                            datepicker { LocalDate.now() }
                        }
                    }

                    button("Create") {

                    }
                }
                vbox {
                    visibleWhen { radioImport.selectedProperty() }
                }
            }
        }
    }

    //companion object : ViewStageFactory<CompetitionCreationWizard>("views/competition-creation-wizard.fxml")

}