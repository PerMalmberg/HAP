package hap.ruleengine.editor.view.css

import javafx.geometry.Pos
import javafx.scene.paint.Color
import tornadofx.Stylesheet
import tornadofx.c
import tornadofx.cssclass
import tornadofx.px

class ComponentStyle : Stylesheet() {
    companion object input {
        val input by cssclass()
        val output by cssclass()
        val connectionPointText by cssclass()
        val center by cssclass()
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

        center {
            fill = Color.GRAY
        }
    }
}