package hap.ruleengine.editor.view.parts

import hap.ruleengine.editor.view.css.ComponentStyle
import hap.ruleengine.editor.viewmodel.parts.InputVM
import javafx.beans.binding.Bindings
import javafx.geometry.Point2D
import tornadofx.*

class InputView : ConnectionPointView() {
    private val vm: InputVM by param()

    override val root =
            stackpane {
                rectangle {
                    width = connectionPointSize
                    height = connectionPointSize
                    fill = vm.color
                    addClass(ComponentStyle.input)
                }.apply {
                    myConnectionPoint = this
                }
            }

    val name: String = vm.name.value
}