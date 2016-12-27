package hap.ruleengine.editor.view.parts

import hap.ruleengine.editor.view.css.ComponentCenterStyle
import hap.ruleengine.editor.view.css.ComponentStyle
import javafx.geometry.Insets
import javafx.scene.layout.StackPane
import tornadofx.*


class Component constructor(y: Double, x: Double) : Fragment() {
    val size = 6.0

    override val root = stackpane {
        borderpane {
            left {
                group {
                    for (i in 0..5) {
                        borderpane {
                            layoutY = i * (size * 2)
                            left {
                                text("Test text " + i.toString()) {
                                    addClass(ComponentStyle.connectionPointText)
                                }
                            }
                            center {
                                rectangle(0.0, 0.0, size, size) {
                                    addClass(ComponentStyle.input)
                                }
                            }
                        }
                    }
                }
            }
            center {
                stackpane {
                    setPrefSize(10.0, 10.0)
                    rectangle() {
                        fill = c("#AA00EE")
                        val p = this.parent as StackPane
                        widthProperty().bind(p.widthProperty())
                        heightProperty().bind(p.heightProperty())
                    }
                }
            }

            right {
                margin = Insets(0.0, 0.0, 0.0, -1.0)
                group {
                    for (i in 0..5) {
                        borderpane {
                            layoutY = i * (size * 2)

                            left {
                                rectangle(0.0, 0.0, size, size) {
                                    addClass(ComponentStyle.output)
                                }
                            }
                            center {
                                text("Test text " + i.toString()) {
                                    addClass(ComponentStyle.connectionPointText)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    init {
        importStylesheet(ComponentStyle::class)
        importStylesheet(ComponentCenterStyle::class)

        with(root)
        {
            this.layoutX = x
            this.layoutY = y
        }
    }
}
