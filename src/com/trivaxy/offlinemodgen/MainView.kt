package com.trivaxy.offlinemodgen

import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Control
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Priority
import javafx.stage.DirectoryChooser
import tornadofx.*
import java.io.File
import javax.xml.soap.Text

class MainView : View() {

    // if you want to assign stuff:
    // var myThing : XTYPE by singleAssign()
    // singleAssign essentially makes var a val again
    // but you can now assign inside the root override or initializer
    // (no runtime errors, but the var will never change again if you try assigning again)
    // (in root simply do myThing = hbox { ... blabla  ... } like done now..
    var directoryField: TextField by singleAssign()
    var modNameField: TextField by singleAssign()
    var displayNameField: TextField by singleAssign()
    var modVersionField: TextField by singleAssign()
    var modAuthorField: TextField by singleAssign()
    var modLangVersionField: TextField by singleAssign()
    var gitIgnoreField: TextField by singleAssign()
    var generateModBtn: Button by singleAssign()
    var statusLabel: Label by singleAssign()

    override val root = borderpane {
        title = "tModLoader Offline Mod Skeleton Generator"

        top = hbox {
            button("Choose Directory") {
                action {
                    val chooser = DirectoryChooser()
                    val dir: File? = chooser.showDialog(currentStage)

                    if (dir == null) {
                        Controller.DIRECTORY = ""
                        directoryField.text = "Select directory path..."
                    }
                    else {
                        Controller.DIRECTORY = dir.absolutePath
                        directoryField.text = Controller.DIRECTORY
                    }
                }
            }

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
                        modLangVersionField = textfield {
                            promptText = "6"
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
                statusLabel = label("Status: Idle") {
                    alignment = Pos.CENTER_LEFT
                    paddingLeft = 5
                }
            }

            pane {
                hgrow = Priority.ALWAYS
            }

            hbox {
                alignment = Pos.CENTER_RIGHT
                generateModBtn = button("Generate mod") {
                    action {
                        // TODO: Move to a seperate function to keep things clean
                        Controller.MOD_NAME = modNameField.text
                        Controller.MOD_VERSION = modVersionField.text
                        Controller.DIRECTORY = directoryField.text
                        Controller.MOD_AUTHOR = modAuthorField.text
                        Controller.MOD_DISPLAY_NAME = displayNameField.text
                        Controller.MOD_LANGUAGE_VERSION = modLangVersionField.text
                        Controller.GIT_IGNORE = gitIgnoreField.text
                        statusLabel.text = Controller.getStatusMessage()
                        Controller.generateModSkeleton()
                    }
                }
            }
        }
    }
}