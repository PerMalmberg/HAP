package hap.ruleengine.editor.view

import hap.ruleengine.editor.view.parts.ComponentView
import hap.ruleengine.editor.viewmodel.ComponentPallet
import hap.ruleengine.editor.viewmodel.event.DragComponentFromComponentPallet
import hap.ruleengine.editor.viewmodel.event.EndDragComponentFromPallet
import javafx.event.EventHandler
import javafx.scene.control.TitledPane
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
                                setOnDragDetected {
                                    this.startFullDrag()
                                    fire(DragComponentFromComponentPallet(vm.componentType))
                                    it.consume()
                                }

                                setOnMouseReleased {
                                    fire(EndDragComponentFromPallet)
                                }

                                this += find<ComponentView>("vm" to it)
                            }
                        }
                    }
            )
        }
    }
}
