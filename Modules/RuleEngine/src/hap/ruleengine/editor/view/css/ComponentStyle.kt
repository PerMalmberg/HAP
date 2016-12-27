package hap.ruleengine.editor.view.css

import javafx.geometry.Pos
import tornadofx.Stylesheet
import tornadofx.c
import tornadofx.cssclass
import tornadofx.px

class ComponentStyle : Stylesheet() {
    companion object input {
        val input by cssclass()
        val output by cssclass()
        val connectionPointText by cssclass()
    }

    init {
        input {
            fill = c("#005555")
        }

        output {
            fill = c("#000055")
        }

        connectionPointText {
            fontSize = 8.px
            fontFamily = "Consolas"
        }
    }
}