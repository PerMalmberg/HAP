package hap.ruleengine.editor.view.parts

import hap.ruleengine.editor.view.css.ComponentStyle
import hap.ruleengine.editor.viewmodel.parts.OutputVM
import tornadofx.*


class OutputView : ConnectionPointView() {
    private val vm: OutputVM by param()

    override val root =
            hbox {
                // TODO: Why are inputs and outputs aligned to top and not offset by connectionPointSize ?
                val y = connectionPointSize + vm.index * 2 * connectionPointSize
                layoutY = y

                rectangle(0.0, y, connectionPointSize, connectionPointSize)
                {
                    fill = vm.color
                    addClass(ComponentStyle.output)
                }.apply {
                    myConnectionPoint = this
                }

                text {
                    textProperty().bind(vm.name)
                    addClass(ComponentStyle.connectionPointText)
                }
            }

    fun connect(targetInput: InputView): WireView {
        return WireView(this, targetInput)
    }

    val name: String = vm.name.value
}
