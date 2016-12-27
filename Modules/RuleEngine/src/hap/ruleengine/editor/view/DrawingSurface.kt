package hap.ruleengine.editor.view

import hap.ruleengine.editor.viewmodel.ComponentVM
import tornadofx.Fragment
import tornadofx.group
import tornadofx.pane
import tornadofx.plusAssign


class DrawingSurface : Fragment() {
    override val root = pane {
        this.minWidth = 400.0
        this.minHeight = 400.0
    }



    init {



        with(root) {
            group {
                this += hap.ruleengine.editor.view.parts.ComponentView(100.0, 100.0, ComponentVM(null))
            }
        }
    }

}
