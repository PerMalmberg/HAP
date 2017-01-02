package hap.ruleengine.editor.view.parts

import hap.ruleengine.editor.view.css.ComponentStyle
import hap.ruleengine.editor.viewmodel.InputVM
import tornadofx.*

const val connectionPointSize : Double = 6.0

class InputView constructor(vm: InputVM) : Fragment() {
    override val root =
            hbox {
                // TODO: Why are inputs and outputs aligned to top and not offset by connectionPointSize ?
                layoutY = connectionPointSize + vm.index * 2 * connectionPointSize
                text {
                    textProperty().bind(vm.name)
                    addClass(ComponentStyle.connectionPointText)
                }

                rectangle(0.0, 0.0, connectionPointSize, connectionPointSize) {
                    fill = vm.color
                    addClass(ComponentStyle.input)
                }
            }


}