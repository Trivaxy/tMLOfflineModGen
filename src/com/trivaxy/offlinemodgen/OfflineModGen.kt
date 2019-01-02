package com.trivaxy.offlinemodgen

import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage
import tornadofx.*

class OfflineModGen : App(MainView::class) {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch<OfflineModGen>(args)
        }
    }

    override fun start(stage: Stage) {
        super.start(stage)
        stage.icons += Image(javaClass.getResource("icon.png").toExternalForm())
    }

    override fun createPrimaryScene(view: UIComponent) = Scene(view.root, 505.0, 255.0)
}