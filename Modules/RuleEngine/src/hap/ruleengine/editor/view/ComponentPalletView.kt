package hap.ruleengine.editor.view

import hap.ruleengine.editor.view.parts.ComponentView
import hap.ruleengine.editor.viewmodel.ComponentVM
import tornadofx.DataGrid
import tornadofx.Fragment
import tornadofx.plusAssign
import tornadofx.stackpane


class ComponentPalletView : Fragment() {
    override val root = DataGrid<ComponentVM>(listOf(ComponentVM(null),ComponentVM(null),ComponentVM(null)))

    init {
        with(root)
        {
            setPrefSize(200.0, 0.0)
            cellCache {
                stackpane {
                    this += ComponentView(0.0, 0.0, it)
                }
            }
        }
    }
}
