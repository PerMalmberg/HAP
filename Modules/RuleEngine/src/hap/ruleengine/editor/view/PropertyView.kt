package hap.ruleengine.editor.view

import tornadofx.Fragment
import tornadofx.stackpane
import tornadofx.vbox


class PropertyView : Fragment() {
    override val root = stackpane {
        vbox {
            this += ComponentPropertyEditor::class
            this += PropertyEditorView::class
            this += DebugView::class
        }
    }
}
