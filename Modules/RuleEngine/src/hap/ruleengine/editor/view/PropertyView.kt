package hap.ruleengine.editor.view

import tornadofx.Fragment
import tornadofx.stackpane
import tornadofx.vbox


class PropertyView : Fragment() {
    override val root = stackpane {
        vbox {
            this += BasicPropertyEditor::class
            this += DebugView::class
        }
    }
}
