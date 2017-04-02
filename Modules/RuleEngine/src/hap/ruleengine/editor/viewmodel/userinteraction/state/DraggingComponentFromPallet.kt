package hap.ruleengine.editor.viewmodel.userinteraction.state

import hap.ruleengine.editor.viewmodel.IDrawingSurfaceView
import hap.ruleengine.editor.viewmodel.event.MouseDragDropReleased
import hap.ruleengine.editor.viewmodel.parts.ComponentVM
import hap.ruleengine.editor.viewmodel.userinteraction.UserInteractionFSM
import hap.ruleengine.parts.ComponentFactory
import hap.ruleengine.parts.composite.CompositeComponent

class DraggingComponentFromPallet constructor(val componentType: String, fsm: UserInteractionFSM) : UserInteractionState(fsm)
{
	private val factory = ComponentFactory()

	override fun mouseDragDropReleased(event: MouseDragDropReleased, view: IDrawingSurfaceView, currentCC: CompositeComponent)
	{
		val c = factory.createFromName(componentType, currentCC, fsm.liveComponents())
		if (c != null)
		{
			// Component has been created, now visualize it.
			val vm = ComponentVM(c)

			val offset = view.getScrollOffset()
			val localPosition = view.sceneToLocal(event.sceneX - offset.x, event.sceneY - offset.y)

			vm.x.value = localPosition.x
			vm.y.value = localPosition.y

			view.add(vm)
		}

		fsm.setState(NoAction(fsm))
	}

	override fun endDragComponentFromComponentPallet()
	{
		fsm.setState(NoAction(fsm))
	}
}