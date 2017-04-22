package hap.ruleengine.editor.viewmodel.userinteraction.state

import chainedfsm.EnterLeaveState
import hap.ruleengine.editor.view.parts.ComponentView
import hap.ruleengine.editor.view.parts.WireView
import hap.ruleengine.editor.viewmodel.DrawingSurfaceVM
import hap.ruleengine.editor.viewmodel.IDrawingSurfaceView
import hap.ruleengine.editor.viewmodel.event.ComponentDragged
import hap.ruleengine.editor.viewmodel.event.MouseDragDropReleased
import hap.ruleengine.editor.viewmodel.event.SelectedComponentsChanged
import hap.ruleengine.editor.viewmodel.parts.ComponentVM
import hap.ruleengine.editor.viewmodel.userinteraction.IUserInteraction
import hap.ruleengine.editor.viewmodel.userinteraction.UserInteractionFSM
import hap.ruleengine.parts.ComponentFactory
import hap.ruleengine.parts.IConnectionPoint
import hap.ruleengine.parts.composite.CompositeComponent
import javafx.geometry.Point2D
import javafx.stage.Window
import tornadofx.FX

abstract class UserInteractionState constructor(val fsm: UserInteractionFSM) : EnterLeaveState(), IUserInteraction
{
	override fun deleteComponent(component: ComponentView)
	{

	}

	override fun deleteWire(wire: WireView)
	{

	}

	override fun updateDragWire(sceneX: Double, sceneY: Double)
	{

	}

	override fun mouseEnteredConnectionPoint(connectionPoint: IConnectionPoint?)
	{

	}

	override fun beginConnectWire(connectionPoint: IConnectionPoint, sceneRelativeCenter: Point2D)
	{

	}

	protected fun createComponentLibraryFolder()
	{
		ComponentFactory.STANDARD_LIBRARY.toFile().mkdirs()
	}

	override fun mouseReleased()
	{
		// Reset any action when user release the mouse button
		fsm.setState(NoAction(fsm))
	}

	override fun componentDragged(dragged: ComponentDragged)
	{
		// Nothing to do here
	}

	override fun selectComponent(component: ComponentVM, addToOrRemoveFromSelection: Boolean)
	{
		if (addToOrRemoveFromSelection)
		{
			setSelectionState(component, !component.isSelected)
		}
		else
		{
			// Only the clicked component should remain selected
			fsm.selectedComponents.values.forEach {
				it.isSelected = false
			}
			fsm.selectedComponents.clear()

			setSelectionState(component, true)
		}

		// Let those listening for changes in selected components know of the new selection state
		FX.eventbus.fire(SelectedComponentsChanged(fsm.selectedComponents))
	}

	override fun openComposite(surface: DrawingSurfaceVM, window: Window)
	{
		fsm.setState(OpenComposite(fsm, surface, window))
	}

	override fun newComposite(surface: DrawingSurfaceVM, window: Window)
	{
		fsm.setState(NewComposite(fsm, surface, window));
	}

	override fun saveComposite(surface: DrawingSurfaceVM, window: Window)
	{
		fsm.setState(SaveComposite(fsm, surface, window))
	}

	override fun mouseDragDropReleased(event: MouseDragDropReleased, view: IDrawingSurfaceView, currentCC: CompositeComponent)
	{
		fsm.setState(NoAction(fsm))
	}

	override fun dragComponentFromComponentPallet(componentType: String)
	{
		fsm.setState(DraggingComponentFromPallet(componentType, fsm))
	}

	override fun dragCompositeFromComponentPallet(sourceFile: String)
	{
		fsm.setState(DraggingCompositeFromPallet(sourceFile, fsm))
	}

	override fun endDragComponentFromComponentPallet()
	{

	}

	protected fun deselectAllComponents()
	{
		fsm.selectedComponents.toList().forEach { deselectComponent(it.second) }
	}

	protected fun setSelectionState(component: ComponentVM, isSelected: Boolean)
	{
		// Select/deselect component
		component.isSelected = isSelected

		if (component.isSelected)
		{
			fsm.selectedComponents.put(component.component.id, component)
		}
		else
		{
			fsm.selectedComponents.remove(component.component.id)
		}
	}

	protected fun deselectComponent(vm: ComponentVM)
	{
		setSelectionState(vm, false)
		FX.eventbus.fire(SelectedComponentsChanged(fsm.selectedComponents))
	}
}
