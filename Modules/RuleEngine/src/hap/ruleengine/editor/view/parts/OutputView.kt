package hap.ruleengine.editor.view.parts

import hap.ruleengine.editor.view.css.ComponentStyle
import hap.ruleengine.editor.viewmodel.parts.OutputVM
import tornadofx.*


class OutputView constructor(val vm: OutputVM) : Fragment() {
    override val root =
            hbox {
                // TODO: Why are inputs and outputs aligned to top and not offset by connectionPointSize ?
                layoutY = connectionPointSize + vm.index * 2 * connectionPointSize
                rectangle(0.0, 0.0, connectionPointSize, connectionPointSize)
                {
                    fill = vm.color
                    addClass(ComponentStyle.output)
                }

                text {
                    textProperty().bind( vm.name )
                    addClass(ComponentStyle.connectionPointText)
                }
            }
}
