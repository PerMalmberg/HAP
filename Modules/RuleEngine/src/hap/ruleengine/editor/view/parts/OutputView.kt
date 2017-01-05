package hap.ruleengine.editor.view.parts

import hap.ruleengine.editor.view.css.ComponentStyle
import hap.ruleengine.editor.viewmodel.parts.OutputVM
import tornadofx.*

class OutputView : ConnectionPointView() {
    private val vm: OutputVM by param()

    override val root =
            stackpane {
                rectangle(0.0, 0.0, connectionPointSize, connectionPointSize)
                {
                    fill = vm.color
                    addClass(ComponentStyle.output)
                }.apply {
                    myConnectionPoint = this
                }
            }

    fun connect(targetInput: InputView): WireView {
        return WireView(this, targetInput)
    }

    val name: String = vm.name.value
}
