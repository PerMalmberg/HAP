package hap.ruleengine.editor.view.parts

import hap.ruleengine.editor.view.css.ComponentStyle
import hap.ruleengine.editor.viewmodel.ComponentVM
import javafx.scene.layout.StackPane
import tornadofx.*


class ComponentView constructor(y: Double, x: Double, vm: ComponentVM) : Fragment() {
    val connectionPointSize = 6.0

    override val root = group {
        borderpane {
            left {
                group {
                    for (i in 0..5) {
                        hbox {
                            layoutY = i * (connectionPointSize * 2)

                            text("Test text " + i.toString()) {
                                addClass(ComponentStyle.connectionPointText)
                            }

                            rectangle(0.0, 0.0, connectionPointSize, connectionPointSize) {
                                addClass(ComponentStyle.input)
                            }

                        }
                    }
                }
            }

            center {
                stackpane {
                    setPrefSize(10.0, 10.0)
                    rectangle() {
                        val p = this.parent as StackPane
                        widthProperty().bind(p.widthProperty())
                        heightProperty().bind(p.heightProperty())
                        addClass(ComponentStyle.center)
                    }
                }
            }

            right {
                group {
                    for (i in 0..5) {
                        hbox {
                            layoutY = i * (connectionPointSize * 2)

                            rectangle(0.0, 0.0, connectionPointSize, connectionPointSize) {
                                addClass(ComponentStyle.output)
                            }

                            text("Test text " + i.toString()) {
                                addClass(ComponentStyle.connectionPointText)
                            }
                        }
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
