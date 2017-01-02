package hap.ruleengine.editor.view.parts

import hap.ruleengine.editor.view.css.ComponentStyle
import hap.ruleengine.editor.viewmodel.parts.ComponentVM
import javafx.scene.layout.StackPane
import tornadofx.*


class ComponentView constructor(x: Double, y: Double, vm: ComponentVM) : Fragment() {

    override val root = group {
        borderpane {
            layoutXProperty().bind( vm.x )
            layoutYProperty().bind( vm.y )

            left {
                group {
                    for (input in vm.inputs) {
                        this += InputView(input)
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
                    }
                }
            }

            right {
                group {
                    for (output in vm.outputs) {
                        this += OutputView(output)
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
}
