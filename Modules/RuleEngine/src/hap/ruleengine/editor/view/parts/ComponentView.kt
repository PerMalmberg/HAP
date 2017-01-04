package hap.ruleengine.editor.view.parts

import hap.ruleengine.editor.view.css.ComponentStyle
import hap.ruleengine.editor.viewmodel.event.SelectComponent
import hap.ruleengine.editor.viewmodel.parts.ComponentVM
import javafx.scene.layout.StackPane
import tornadofx.*
import java.util.*


class ComponentView : Fragment() {

    val vm: ComponentVM by param()
    private val myOutputs: ArrayList<OutputView> = ArrayList()
    private val myInputs: ArrayList<InputView> = ArrayList()

    override val root = group {
        borderpane {
            layoutXProperty().bind(vm.x)
            layoutYProperty().bind(vm.y)

            left {
                group {
                    vm.inputs.filter { it.connectionPoint.isVisibleWhenParentIsVisualized }.map {
                        val inp = InputView(it)
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

                        setOnMouseClicked {
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
                    vm.outputs.filter { it.connectionPoint.isVisibleWhenParentIsVisualized }.map {
                        val out = OutputView(it)
                        myOutputs.add(out)
                        this += out
                    }
                }
            }
        }
    }

    init {
        importStylesheet(ComponentStyle::class)
    }

    fun getInputView(nameOfInput: String): InputView? {
        return myInputs.singleOrNull { nameOfInput == it.vm.name.value }
    }

    fun getOutputView(nameOfOutput: String): OutputView? {
        return myOutputs.singleOrNull { nameOfOutput == it.vm.name.value }
    }
}
