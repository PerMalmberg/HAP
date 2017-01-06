package hap.ruleengine.editor.view

import hap.ruleengine.editor.view.css.ComponentStyle
import hap.ruleengine.editor.view.parts.ComponentView
import hap.ruleengine.editor.viewmodel.DrawingSurfaceVM
import hap.ruleengine.editor.viewmodel.IDrawingSurfaceView
import hap.ruleengine.editor.viewmodel.event.MouseDragReleased
import hap.ruleengine.editor.viewmodel.parts.ComponentVM
import hap.ruleengine.parts.composite.CompositeComponent
import javafx.geometry.Point2D
import javafx.scene.Group
import javafx.scene.input.TransferMode
import javafx.scene.shape.Rectangle
import tornadofx.*
import java.util.*

class DrawingSurface : Fragment(), IDrawingSurfaceView {
    private val vm: DrawingSurfaceVM by inject()

    private var drawingBackground: Rectangle by singleAssign()
    private var wireBackground: Rectangle by singleAssign()
    private var componentLayer: Group by singleAssign()
    private var wireLayer: Group by singleAssign()
    val components: HashMap<UUID, ComponentView> = HashMap()
    val drawingSize = 5000.0

    private fun getComponentView(id: UUID): ComponentView? {
        return components[id]
    }

    override fun sceneToLocal(sceneX: Double, sceneY: Double): Point2D {
        return root.sceneToLocal(sceneX, sceneY)
    }

    override fun add(vm: ComponentVM) {
        val cv = find<ComponentView>("vm" to vm)
        componentLayer += cv
        components.put(cv.vm.component.id, cv)
    }

    override fun clearComponents() {
        componentLayer.children.clear()
        components.clear()
        componentLayer += drawingBackground
        wireLayer.children.clear()
        wireLayer += wireBackground
    }

    override val root = scrollpane {
        stackpane {
            group {
                rectangle {
                    layoutX = 0.0
                    layoutY = 0.0
                    width = drawingSize
                    height = drawingSize
                    addClass(ComponentStyle.wireLayerBackground)
                }.apply {
                    wireBackground = this
                }
            }.apply {
                wireLayer = this
            }

            group {
                rectangle {
                    layoutX = 0.0
                    layoutY = 0.0
                    width = drawingSize
                    height = drawingSize
                    addClass(ComponentStyle.drawingBackground)
                }.apply {
                    drawingBackground = this
                }

                setOnMouseDragReleased {
                    fire(MouseDragReleased(it.sceneX, it.sceneY))
                }

                setOnDragOver {
                    it.acceptTransferModes(TransferMode.COPY)
                }
            }.apply {
                componentLayer = this
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
                wireLayer += wire
                wire.updateEndPoints()
            }
        }

    }
}
