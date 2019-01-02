package com.trivaxy.offlinemodgen.view

import com.trivaxy.offlinemodgen.controller.RootController
import com.trivaxy.offlinemodgen.model.GenerationModel
import javafx.collections.FXCollections
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.Priority
import tornadofx.*

class RootView : View() {

    var directoryButton: Button by singleAssign()
    var directoryField: TextField by singleAssign()
    var modNameField: TextField by singleAssign()
    var displayNameField: TextField by singleAssign()
    var modVersionField: TextField by singleAssign()
    var modAuthorField: TextField by singleAssign()
    var modLangVersionField: ComboBox<Int> by singleAssign()
    var gitIgnoreField: TextField by singleAssign()
    var generateModBtn: Button by singleAssign()
    var statusLabel: Label by singleAssign()

    private val controller: RootController by inject()

    override val root = borderpane {
        title = "tModLoader Offline Mod Skeleton Generator"

        top = hbox {
            directoryButton = button("Choose Directory")
            directoryField = textfield {
                hgrow = Priority.ALWAYS
                promptText = "Select directory path..."
            }
        }

        center = form {
            hbox(20) {
                fieldset("Mod details") {
                    field("Mod name") {
                        modNameField = textfield()
                    }
                    field("Display name") {
                        displayNameField = textfield()
                    }
                    field("Mod version") {
                        modVersionField = textfield()
                    }
                    field("Mod author") {
                        modAuthorField = textfield()
                    }
                }

                fieldset("Extras") {
                    field("Language version") {
                        modLangVersionField = combobox {
                            items = FXCollections.observableArrayList(4, 6)
                            selectionModel.selectFirst()
                        }
                    }
                    field("Build ignore") {
                        gitIgnoreField = textfield {
                            promptText = "*.csproj, *.sln, ..."
                        }
                    }
                }
            }
        }

        bottom = hbox {
            hbox {
                statusLabel = label("Idle") {
                    alignment = Pos.CENTER_LEFT
                    paddingLeft = 5
                }
            }

            pane {
                hgrow = Priority.ALWAYS
            }

            hbox {
                alignment = Pos.CENTER_RIGHT
                generateModBtn = button("Generate mod")
            }
        }
    }

    init {
        directoryButton.action { handleDirectoryButton() }
        generateModBtn.action { handleGenerateModSkeleton() }
    }

    private fun handleDirectoryButton() {
        val directory = controller.chooseDirectory(currentStage)
        if (directory != null) {
            directoryField.text = directory
        }
    }

    private fun handleGenerateModSkeleton() {
        with(GenerationModel(
                modNameField.text,
                displayNameField.text,
                modVersionField.text,
                modAuthorField.text,
                modLangVersionField.selectionModel.selectedItem.toString(),
                directoryField.text,
                gitIgnoreField.text
        )) {
            statusLabel.text = controller.generateModSkeleton(this)
        }
    }
}