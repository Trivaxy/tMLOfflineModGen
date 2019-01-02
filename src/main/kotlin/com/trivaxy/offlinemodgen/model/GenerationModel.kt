package com.trivaxy.offlinemodgen.model

data class GenerationModel(

        var modName: String,
        var modDisplayName: String,
        var modVersion: String,
        var modAuthor: String,
        var modLanguageVersion: String,
        var outputPath: String,
        var modBuildPropertiesGitIgnore: String

)