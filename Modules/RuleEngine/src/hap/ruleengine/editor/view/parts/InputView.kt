package hap.ruleengine.editor.view.parts

import hap.ruleengine.editor.view.css.ComponentStyle
import hap.ruleengine.editor.viewmodel.parts.InputVM
import javafx.beans.binding.Bindings
import javafx.geometry.Point2D
import tornadofx.*

class InputView : ConnectionPointView() {
    private val vm: InputVM by param()
    private val yIndex : Int by param()

    override val root =
            hbox {
                // TODO: Why are inputs and outputs aligned to top and not offset by connectionPointSize ?
                val y = connectionPointSize + vm.index * 2 * connectionPointSize
                layoutY = y

                text {
                    textProperty().bind(vm.name)
                    addClass(ComponentStyle.connectionPointText)
                }

                rectangle(0.0, y, connectionPointSize, connectionPointSize) {
                    fill = vm.color
                    addClass(ComponentStyle.input)
                }.apply {
                    myConnectionPoint = this
                }
            }

    val name: String = vm.name.value
}