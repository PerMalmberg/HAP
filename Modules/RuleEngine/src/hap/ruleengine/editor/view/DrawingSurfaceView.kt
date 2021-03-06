package hap.ruleengine.editor.view

import hap.ruleengine.editor.view.css.ComponentStyle
import hap.ruleengine.editor.view.parts.ComponentView
import hap.ruleengine.editor.view.parts.WireView
import hap.ruleengine.editor.viewmodel.DrawingSurfaceVM
import hap.ruleengine.editor.viewmodel.IDrawingSurfaceView
import hap.ruleengine.editor.viewmodel.event.MouseDragDropReleased
import hap.ruleengine.editor.viewmodel.event.SetLiveComponentsEvent
import hap.ruleengine.editor.viewmodel.parts.ComponentVM
import hap.ruleengine.parts.composite.CompositeComponent
import hap.ui.event.AppShutdownEvent
import javafx.geometry.Point2D
import javafx.scene.Group
import javafx.scene.control.ScrollPane
import javafx.scene.input.TransferMode
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import javafx.scene.shape.Rectangle
import tornadofx.*
import java.util.*

class DrawingSurfaceView : Fragment(), IDrawingSurfaceView
{
	private val vm = DrawingSurfaceVM()

	private var drawingBackground: Rectangle by singleAssign()

	private var wireBackground: Rectangle by singleAssign()

	private var componentLayer: Group by singleAssign()
	private var wireLayer: Group by singleAssign()
	private var dragLine: Line by singleAssign()
	val components: HashMap<UUID, ComponentView> = HashMap()
	val drawingSize = 5000.0
	private var pane: ScrollPane by singleAssign()
	override fun getVM() = vm

	private fun getComponentView(id: UUID): ComponentView?
	{
		return components[id]
	}

	override fun delete(wire: WireView)
	{
		wire.delete()
		vm.delete(wire.wire)
	}

	override fun deleteComponent(component: ComponentView)
	{
		components.remove(component.vm.component.id)
		vm.deleteComponent(component)
	}

	override fun sceneToLocal(sceneX: Double, sceneY: Double): Point2D
	{
		return root.sceneToLocal(sceneX, sceneY)
	}

	override fun add(vm: ComponentVM)
	{
		val cv = find<ComponentView>(mapOf(ComponentView::vm to vm))
		componentLayer += cv
		components.put(cv.vm.component.id, cv)
	}

	override fun clearComponents()
	{
		componentLayer.children.clear()
		components.clear()
		componentLayer += drawingBackground
		wireLayer.children.clear()
		wireLayer += wireBackground
		wireLayer += dragLine
	}

	override fun getScrollOffset(): Point2D
	{
		// Force a layout update so that pane.viewportBounds.minX/Y is updated
		pane.requestLayout()
		pane.layout()
		return Point2D(pane.viewportBounds.minX, pane.viewportBounds.minY)
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

				line {
					stroke = Color.GRAY
					strokeWidth = 2.0
					strokeDashArray.addAll(10.0, 5.0)
					isVisible = false
				}.apply {
					dragLine = this
					startXProperty().bind(vm.dragLineStartXProperty())
					startYProperty().bind(vm.dragLineStartYProperty())
					endXProperty().bind(vm.dragLineEndXProperty())
					endYProperty().bind(vm.dragLineEndYProperty())
					visibleProperty().bind(vm.dragLineVisibleProperty())
				}

				setOnMouseDragReleased {
					fire(MouseDragDropReleased(it.sceneX, it.sceneY))
				}

				setOnDragOver {
					it.acceptTransferModes(TransferMode.COPY)
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
					isMouseTransparent = true
				}.apply {
					drawingBackground = this
				}
			}.apply {
				componentLayer = this
			}
		}
	}.apply {
		pane = this
	}


	init
	{
		vm.init(this)

		subscribe<SetLiveComponentsEvent> {
			vm.toggleLiveComponents(it.isLive)
		}

		subscribe<AppShutdownEvent> {
			vm.shutdown()
		}
	}

	override fun drawWires(cc: CompositeComponent)
	{
		cc.wires.forEach {
			val sourceComponent = getComponentView(it.sourceComponent)
			val sourceOutput = sourceComponent?.getOutputView(it.sourceOutput)
			val targetComponent = getComponentView(it.targetComponent)
			val targetInput = targetComponent?.getInputView(it.targetInput)

			if (sourceOutput != null && targetInput != null)
			{
				// Since an input only can have one input we use that as a check to see which wires are not yet visualized
				if (!targetInput.hasWires())
				{
					val wire = sourceOutput.connect(targetInput)
					wire.wire = it
					wireLayer += wire
					wire.updateEndPoints()
				}
			}
		}
	}
}
