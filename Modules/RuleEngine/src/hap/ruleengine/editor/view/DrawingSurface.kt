package hap.ruleengine.editor.view

import hap.ruleengine.editor.view.parts.ComponentView
import hap.ruleengine.editor.viewmodel.DrawingSurfaceVM
import hap.ruleengine.editor.viewmodel.IDrawingSurfaceView
import hap.ruleengine.editor.viewmodel.event.EndComponentCreation
import hap.ruleengine.editor.viewmodel.parts.ComponentVM
import hap.ruleengine.parts.composite.CompositeComponent
import javafx.geometry.Point2D
import javafx.scene.Group
import javafx.scene.input.TransferMode
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import tornadofx.*
import java.util.*

class DrawingSurface : Fragment(), IDrawingSurfaceView {
    private val vm: DrawingSurfaceVM by inject()

    private var rect: Rectangle by singleAssign()
    private var group: Group by singleAssign()
    val components: HashMap<UUID, ComponentView> = HashMap()

    private fun getComponentView(id: UUID): ComponentView? {
        return components.get(id)
    }

    override fun sceneToLocal(sceneX: Double, sceneY: Double): Point2D {
        return root.sceneToLocal(sceneX, sceneY)
    }

    override fun add(vm: ComponentVM) {
        val cv = find<ComponentView>("vm" to vm)
        group += cv
        components.put(cv.vm.component.id, cv)
    }

    override fun clearComponents() {
        group.children.clear()
        components.clear()
        group += rect
    }

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

    override fun drawWires(cc: CompositeComponent) {
        cc.wires.forEach {
            val sourceComponent = getComponentView(it.sourceComponent)
            val sourceOutput = sourceComponent?.getOutputView(it.sourceOutput)
            val targetComponent = getComponentView(it.targetComponent)
            val targetInput = targetComponent?.getInputView(it.targetInput)

            if (sourceOutput != null && targetInput != null) {
                val wire = sourceOutput.connect(targetInput)
                group += wire
                wire.updateEndPoints()
            }
        }

    }
}
