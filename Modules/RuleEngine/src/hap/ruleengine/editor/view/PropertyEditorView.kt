package hap.ruleengine.editor.view

import hap.ruleengine.editor.viewmodel.ViewBuilder
import hap.ruleengine.editor.viewmodel.event.SelectedComponentsChanged
import tornadofx.*

class PropertyEditorView : Fragment() {

    override val root = borderpane {
        center {
            stackpane {
                subscribe<SelectedComponentsChanged> {
                    this.replaceChildren {
                        if (it.selectedComponents.size == 1) {
                            val single = it.selectedComponents.values.first()
                            val vb = ViewBuilder(this)
                            single.component.showProperties(vb)
                        }
                    }
                }
            }
        }
    }
}

