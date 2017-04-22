package hap.ruleengine.editor.viewmodel

import hap.ruleengine.editor.view.parts.ComponentView
import hap.ruleengine.editor.viewmodel.event.*
import hap.ruleengine.editor.viewmodel.parts.ComponentVM
import hap.ruleengine.editor.viewmodel.userinteraction.UserInteractionFSM
import hap.ruleengine.parts.CompositeSerializer
import hap.ruleengine.parts.IConnectionPoint
import hap.ruleengine.parts.Wire.IWire
import hap.ruleengine.parts.composite.CompositeComponent
import javafx.application.Platform
import javafx.geometry.Point2D
import tornadofx.ViewModel
import tornadofx.getProperty
import tornadofx.property
import tornadofx.singleAssign
import java.io.File
import java.util.*
import kotlin.concurrent.timerTask

class DrawingSurfaceVM : ViewModel()
{
	private var interaction: UserInteractionFSM by singleAssign()
	private var currentCC: CompositeComponent = CompositeComponent(UUID.randomUUID(), null, false)
	private val tickTimer = java.util.Timer(true)
	private var liveComponents = false

	var surface: IDrawingSurfaceView by singleAssign()

	fun getLiveComponents() = liveComponents

	init
	{
		subscribeToEvents()

		tickTimer.schedule(timerTask {
			Platform.runLater {
				currentCC.tick()
			}
		}, 10, 10)
	}

	fun init(s: IDrawingSurfaceView)
	{
		surface = s
		interaction = UserInteractionFSM(surface, this)
	}

	private fun subscribeToEvents()
	{
		subscribe<DragCompositeFromComponentPallet> {
			interaction.dragCompositeFromComponentPallet(it.sourcefile)
		}

		subscribe<DragComponentFromComponentPallet> {
			interaction.dragComponentFromComponentPallet(it.componentType)
		}

		subscribe<EndDragComponentFromPallet> {
			interaction.endDragComponentFromComponentPallet()
		}

		subscribe<MouseDragDropReleased> {
			interaction.mouseDragDropReleased(it, surface, currentCC)
		}

		subscribe<OpenCompositeFromFile> {
			interaction.openComposite(this@DrawingSurfaceVM, it.window)
		}

		subscribe<NewComposite> {
			interaction.newComposite(this@DrawingSurfaceVM, it.window)
		}

		subscribe<SelectComponent> {
			interaction.selectComponent(it.component, it.addToOrRemoveFromSelection)
		}

		subscribe<ComponentDragged> {
			interaction.componentDragged(it)
		}

		subscribe<MouseReleased> {
			interaction.mouseReleased()
		}

		subscribe<SaveComposite> {
			interaction.saveComposite(this@DrawingSurfaceVM, it.window)
		}

		subscribe<BeginConnectWire> {
			interaction.beginConnectWire(it.connectionPoint, it.sceneRelativeCenter)
		}

		subscribe<MouseEnteredConnectionPoint> {
			interaction.mouseEnteredConnectionPoint(it.connectionPoint)
		}

		subscribe<UpdateDragWire> {
			interaction.updateDragWire(it.sceneX, it.sceneY)
		}

		subscribe<DeleteWire> {
			interaction.deleteWire(it.wire)
		}

		subscribe<DeleteComponent> {
			interaction.deleteComponent(it.component)
		}
	}

	private fun visualize()
	{
		// Visualize all components in the current composite
		currentCC.components
				.map {
					val vm = ComponentVM(it)
					vm.x.value = it.x
					vm.y.value = it.y
					surface.add(vm)
				}

		// Once all components are visualized, draw the wires.
		// It is run 'later' so that the GridPane has completed its layout phase.
		// If we don't wait for that to complete, we get invalid coordinates when calling boundsInParent etc.
		Platform.runLater {
			surface.drawWires(currentCC)
		}
	}

	fun setComposite(cc: CompositeComponent)
	{
		currentCC.executionState = false
		surface.clearComponents()
		currentCC.tearDown()
		currentCC = cc
		visualize()
		currentCC.executionState = liveComponents
	}

	fun saveComposite(file: File): Boolean
	{
		val serializer = CompositeSerializer()
		return serializer.serialize(currentCC, file)
	}

	fun addWire(startPoint: IConnectionPoint, lastEntered: IConnectionPoint)
	{
		val wire: IWire? = currentCC.addWire(startPoint, lastEntered)
		if (wire != null)
		{
			surface.drawWires(currentCC)
		}
	}

	fun delete(wire: IWire?)
	{
		currentCC.deleteWire(wire)
	}

	fun setDragWire(startSceneRelativeCenter: Point2D, sceneX: Double, sceneY: Double)
	{
		val offset =  surface.getScrollOffset()
		var xy = surface.sceneToLocal(startSceneRelativeCenter.x - offset.x, startSceneRelativeCenter.y - offset.y)
		dragLineStartX = xy.x
		dragLineStartY = xy.y
		xy = surface.sceneToLocal(sceneX - offset.x, sceneY - offset.y)
		dragLineEndX = xy.x
		dragLineEndY = xy.y
		dragLineVisible = true
	}

	fun hideDragWire()
	{
		dragLineVisible = false
	}

	fun deleteComponent(component: ComponentView)
	{
		currentCC.deleteComponent(component.vm.component)
		component.delete()
	}

	var dragLineStartX: Double by property(0.0)
	fun dragLineStartXProperty() = getProperty(DrawingSurfaceVM::dragLineStartX)
	var dragLineStartY: Double by property(0.0)
	fun dragLineStartYProperty() = getProperty(DrawingSurfaceVM::dragLineStartY)
	var dragLineEndX: Double by property(0.0)
	fun dragLineEndXProperty() = getProperty(DrawingSurfaceVM::dragLineEndX)
	var dragLineEndY: Double by property(0.0)
	fun dragLineEndYProperty() = getProperty(DrawingSurfaceVM::dragLineEndY)
	var dragLineVisible: Boolean by property(false)
	fun dragLineVisibleProperty() = getProperty(DrawingSurfaceVM::dragLineVisible)

	fun toggleLiveComponents(live: Boolean)
	{
		liveComponents = live
		currentCC.executionState = live
	}

	fun shutdown()
	{
		toggleLiveComponents(false)
		tickTimer.cancel()
		// Give the component a last tick so that they can shutdown any timers and threads.
		currentCC.tick()
		currentCC.tearDown()
	}

}