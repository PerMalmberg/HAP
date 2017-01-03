package hap.ruleengine.editor.view

import hap.ruleengine.editor.view.parts.ComponentView
import hap.ruleengine.editor.viewmodel.DrawingSurfaceVM
import hap.ruleengine.editor.viewmodel.IDrawingSurfaceView
import hap.ruleengine.editor.viewmodel.event.EndComponentCreation
import javafx.geometry.Point2D
import javafx.scene.Group
import javafx.scene.input.TransferMode
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import tornadofx.*

class DrawingSurface : Fragment(), IDrawingSurfaceView {
    override fun sceneToLocal(sceneX: Double, sceneY: Double): Point2D {
        return root.sceneToLocal(sceneX, sceneY)
    }

    private var group: Group by singleAssign()
    private var rect : Rectangle by singleAssign()

    override fun add(cv: ComponentView) {
        group += cv
    }

    override fun clearComponents() {
        group.children.clear()
        group += rect
    }

    val vm: DrawingSurfaceVM by inject()

    override val root = scrollpane {
        stackpane {
            group {
                rectangle {
                    layoutX = 0.0
                    layoutY = 0.0
                    width = 5000.0
                    height = 5000.0
                    fill = Color.LIGHTGOLDENRODYELLOW
                }.apply {
                    rect = this
                }

                setOnMouseDragReleased {
                    fire(EndComponentCreation(it.sceneX, it.sceneY))
                }

                setOnDragOver {
                    it.acceptTransferModes(TransferMode.COPY)
                }
            }.apply {
                group = this
            }
        }
    }

    init {
        vm.view = this
    }
}
