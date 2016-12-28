package hap.ruleengine.editor.view.parts

import hap.ruleengine.editor.view.css.ComponentStyle
import hap.ruleengine.editor.viewmodel.InputVM
import tornadofx.*

const val connectionPointSize : Double = 6.0

class InputView constructor(vm: InputVM) : Fragment() {

    override val root =
            hbox {
                text {
                    addClass(ComponentStyle.connectionPointText)
                }

                rectangle(0.0, 0.0, connectionPointSize, connectionPointSize)
                {
                    addClass(ComponentStyle.input)
                }
            }


}

