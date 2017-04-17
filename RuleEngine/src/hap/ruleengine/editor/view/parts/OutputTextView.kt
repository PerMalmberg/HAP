package hap.ruleengine.editor.view.parts

import hap.ruleengine.editor.view.css.ComponentStyle
import hap.ruleengine.editor.viewmodel.parts.OutputVM
import tornadofx.*

class OutputTextView : Fragment() {
    val vm: OutputVM by param()

    override val root = stackpane {
        text {
            textProperty().bind(vm.name)
            addClass(ComponentStyle.connectionPointText)
        }
    }
}
