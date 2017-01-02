package hap.ruleengine.editor.view

import hap.ruleengine.editor.view.parts.ComponentView
import hap.ruleengine.editor.viewmodel.ComponentPallet
import hap.ruleengine.editor.viewmodel.event.StartComponentCreation
import javafx.event.EventHandler
import javafx.scene.control.TitledPane
import javafx.scene.input.MouseEvent
import tornadofx.*


class ComponentPalletView : Fragment() {

    val pallet: ComponentPallet by inject()

    override val root = listview(pallet.categories.observable()) {
        prefWidth = 300.0
        cellCache {
            TitledPane(it.category,
                    listview(it.components) {
                        cellCache {
                            stackpane {
                                val vm = it
                                this += ComponentView(0.0, 0.0, it)
                                onDragDetected = EventHandler {
                                    this.startFullDrag()
                                    fire(StartComponentCreation(vm.componentType))
                                    it.consume()
                                }

                                onDragDone = EventHandler {
                                    fire(StartComponentCreation(""))
                                }
                            }
                        }
                    }
            )
        }
    }
}
