package hap.ruleengine.editor.view.parts

import hap.ruleengine.editor.view.css.ComponentStyle
import hap.ruleengine.editor.viewmodel.parts.InputVM
import tornadofx.*

class InputTextView : Fragment() {
    private val vm: InputVM by param()

    override val root = stackpane {
        text {
            textProperty().bind(vm.name)
            addClass(ComponentStyle.connectionPointText)
        }
    }
}
