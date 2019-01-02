package com.trivaxy.offlinemodgen.controller

import com.trivaxy.offlinemodgen.model.GenerationModel
import com.trivaxy.offlinemodgen.service.GeneratorService
import com.trivaxy.offlinemodgen.service.ValidatorService
import javafx.stage.DirectoryChooser
import javafx.stage.Stage
import tornadofx.Controller

class RootController : Controller() {

    private val validatorService: ValidatorService by di()
    private val generatorService: GeneratorService by di()

    fun chooseDirectory(currentStage: Stage?): String? {
        val chooser = DirectoryChooser()
        return chooser.showDialog(currentStage).absolutePath ?: return null
    }

    fun generateModSkeleton(generationModel: GenerationModel): String {
        if (!validatorService.validateGenerationModel(generationModel)) {
            return validatorService.validationError
        }

        if (!generatorService.generateModSkeleton(generationModel)) {
            return generatorService.generationException ?: "Unknown error occurred"
        }

        return "Mod skeleton generated!"
    }
}