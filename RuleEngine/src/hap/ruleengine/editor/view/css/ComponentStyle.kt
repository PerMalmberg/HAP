package hap.ruleengine.editor.view.css

import javafx.geometry.Pos
import javafx.scene.paint.Color
import tornadofx.*

class ComponentStyle : Stylesheet() {
    companion object input {
        val input by cssclass()
        val output by cssclass()
        val connectionPointText by cssclass()
        val componentCenter by cssclass()
        val componentSelected by cssclass()
        val drawingBackground by cssclass()
        val wireLayerBackground by cssclass()
    }

    init {
        input {
            alignment = Pos.TOP_CENTER
        }

        output {
            alignment = Pos.TOP_CENTER
        }

        connectionPointText {
            fontSize = 8.px
            fontFamily = "Consolas"
            alignment = Pos.BASELINE_CENTER
        }

        componentCenter {
            fill = Color.GRAY
        }

        componentSelected {
            fill = Color.ORANGE
        }

        drawingBackground {
            fill = Color.TRANSPARENT
        }

        wireLayerBackground {
            fill = Color.LIGHTGOLDENRODYELLOW
        }
    }
}