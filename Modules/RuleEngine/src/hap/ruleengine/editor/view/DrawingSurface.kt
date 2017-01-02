package hap.ruleengine.editor.view

import hap.ruleengine.editor.viewmodel.DrawingSurfaceVM
import hap.ruleengine.editor.viewmodel.event.EndComponentCreation
import javafx.event.EventHandler
import javafx.scene.Group
import javafx.scene.input.MouseDragEvent
import javafx.scene.input.TransferMode
import javafx.scene.paint.Color
import tornadofx.*


class DrawingSurface : Fragment() {
    val vm: DrawingSurfaceVM = DrawingSurfaceVM()
    var group: Group by singleAssign()

    override val root = scrollpane {
        stackpane {
            group {
                rectangle {
                    layoutX = 0.0
                    layoutY = 0.0
                    width = 5000.0
                    height = 5000.0
                    fill = Color.LIGHTGOLDENRODYELLOW

                    setOnMouseDragReleased {
                        fire(EndComponentCreation(group, it.sceneX, it.sceneY))
                    }

                    setOnDragOver {
                        it.acceptTransferModes(TransferMode.COPY)
                    }
                }
            }.apply {
                group = this
            }
            }
    }


    init {
    }

}
