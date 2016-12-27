package hap.ruleengine.editor.view.css

import tornadofx.*


class ComponentCenterStyle : Stylesheet() {
    companion object {
        val style by cssclass()
    }

    init {
        style {
            fill = c("#8F0011")
            arcWidth = 5.px
            arcHeight = 5.px
        }
    }
}