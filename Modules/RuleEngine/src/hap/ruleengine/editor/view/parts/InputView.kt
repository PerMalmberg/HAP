package hap.ruleengine.editor.view.parts

import hap.ruleengine.editor.view.css.ComponentStyle
import hap.ruleengine.editor.viewmodel.event.UpdateDragWire
import hap.ruleengine.editor.viewmodel.parts.InputVM
import tornadofx.addClass
import tornadofx.rectangle
import tornadofx.stackpane

class InputView : ConnectionPointView() {
    private val vm: InputVM by param()

    override val root =
            stackpane {
                rectangle(0.0, 0.0, connectionPointSize, connectionPointSize) {
                    fill = vm.color
                    addClass(ComponentStyle.input)

                    setOnDragDetected {
                        startConnectWire(vm.connectionPoint, getSceneRelativeCenter())
                        startFullDrag()
                    }

                    setOnMouseDragged {
                        fire(UpdateDragWire( it.sceneX, it.sceneY))
                    }

                    setOnMouseDragExited {
                        enteredConnectionPoint(null)
                    }

                    setOnMouseDragEntered {
                        enteredConnectionPoint(vm.connectionPoint)
                    }

                }.apply {
                    myConnectionPoint = this
                }
            }

    val name: String = vm.name.value
}
