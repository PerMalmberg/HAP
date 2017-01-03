package hap.ruleengine.editor.view.parts

import hap.ruleengine.editor.view.css.ComponentStyle
import hap.ruleengine.editor.viewmodel.IDrawingSurfaceView
import hap.ruleengine.editor.viewmodel.event.SelectComponent
import hap.ruleengine.editor.viewmodel.parts.ComponentVM
import javafx.event.EventHandler
import javafx.scene.layout.StackPane
import tornadofx.*
import java.util.*


class ComponentView constructor(x: Double, y: Double, val vm: ComponentVM) : Fragment() {

    private val myOutputs: ArrayList<OutputView> = ArrayList()
    private val myInputs: ArrayList<InputView> = ArrayList()

    override val root = group {
        borderpane {
            layoutXProperty().bind(vm.x)
            layoutYProperty().bind(vm.y)

            left {
                group {
                    for (input in vm.inputs) {
                        val inp = InputView(input)
                        myInputs.add(inp)
                        this += inp
                    }
                }
            }

            center {
                stackpane {
                    setPrefSize(10.0, 10.0)
                    rectangle {
                        val p = this.parent as StackPane
                        widthProperty().bind(p.widthProperty())
                        heightProperty().bind(p.heightProperty())
                        addClass(ComponentStyle.center)

                        onMouseClicked = EventHandler {
                            fire(SelectComponent(vm))
                        }

                        subscribe<SelectComponent> {
                            if (vm === it.component) {
                                addClass(ComponentStyle.centerSelected)
                            } else {
                                removeClass(ComponentStyle.centerSelected)
                            }
                        }
                    }
                }
            }

            right {
                group {
                    for (output in vm.outputs) {
                        val out = OutputView(output)
                        myOutputs.add(out)
                        this += out
                    }
                }
            }
        }
    }

    init {
        importStylesheet(ComponentStyle::class)

        with(root)
        {
            vm.x.value = x
            vm.y.value = y
        }
    }

    fun drawWires( surface : IDrawingSurfaceView)
    {
        myOutputs.map {
            // Get hold of the all the remote component views

        }
    }
}
