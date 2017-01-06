package hap.ruleengine.editor.viewmodel.userinteraction.state

import chainedfsm.FSM
import hap.ruleengine.editor.viewmodel.IDrawingSurfaceView
import hap.ruleengine.editor.viewmodel.event.MouseDragReleased
import hap.ruleengine.editor.viewmodel.parts.ComponentVM
import hap.ruleengine.editor.viewmodel.userinteraction.state.NoAction
import hap.ruleengine.editor.viewmodel.userinteraction.state.UserInteractionState
import hap.ruleengine.parts.ComponentFactory
import hap.ruleengine.parts.composite.CompositeComponent

class DraggingComponentFromPallet constructor(val componentType: String, fsm: FSM<UserInteractionState>) : UserInteractionState(fsm) {
    private val factory = ComponentFactory()

    override fun mouseDragReleased(event: MouseDragReleased, view: IDrawingSurfaceView, currentCC: CompositeComponent) {
        val c = factory.createFromName(componentType, currentCC)
        if (c != null) {
            // Component has been created, now visualize it.
            val localPosition = view.sceneToLocal(event.sceneX, event.sceneY)
            val vm = ComponentVM(c)
            vm.x.value = localPosition.x
            vm.y.value = localPosition.y
            view.add(vm)
        }

        myFsm.setState(NoAction(myFsm))
    }
}