package com.ironpanthers.scouting.desktop.controller.server

import com.ironpanthers.scouting.common.Competition
import com.ironpanthers.scouting.desktop.util.importTBAData
import javafx.geometry.Pos
import javafx.scene.control.RadioButton
import javafx.scene.control.TextField
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.BorderPane
import org.slf4j.LoggerFactory
import tornadofx.*
import java.time.LocalDate

class CompetitionCreationWizard : View() {
    override val root: BorderPane

    private val creationToggleGroup = ToggleGroup()

    private lateinit var radioCreate: RadioButton
    private lateinit var radioImport: RadioButton

    private val logger = LoggerFactory.getLogger(javaClass)

    var result: Competition? = null

    init {
        var tbaMatchName: TextField? = null

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
                            datepicker {
                                value = LocalDate.now()
                            }
                        }
                    }

                    button("Create") {
                        alignment = Pos.BOTTOM_RIGHT
                    }
                }
                vbox {
                    visibleWhen { radioImport.selectedProperty() }
                    gridpane {
                        row {
                            label("Event ID")
                            tbaMatchName = textfield()
                        }
                    }
                    button("Import") {
                        action {
                            val id = tbaMatchName!!.text
                            logger.info("Attempting to get {} from TBA", id)
                            result = importTBAData(id, System.getProperty("tba-api-key"))
                            logger.debug("recieved: {}", result)
                            modalStage?.close()
                        }
                    }
                }

            }

        }
    }

}