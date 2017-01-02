package hap.ruleengine.editor.view.parts

import hap.ruleengine.editor.view.css.ComponentStyle
import hap.ruleengine.editor.viewmodel.ComponentVM
import javafx.scene.layout.StackPane
import tornadofx.*


class ComponentView constructor(y: Double, x: Double, vm: ComponentVM) : Fragment() {

    override val root = group {
        borderpane {
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
            this.layoutX = x
            this.layoutY = y
        }
    }
}
