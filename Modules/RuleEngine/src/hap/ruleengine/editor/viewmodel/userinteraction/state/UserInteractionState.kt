package hap.ruleengine.editor.viewmodel.userinteraction.state

import chainedfsm.EnterLeaveState
import hap.ruleengine.editor.viewmodel.DrawingSurfaceVM
import hap.ruleengine.editor.viewmodel.IDrawingSurfaceView
import hap.ruleengine.editor.viewmodel.event.ComponentDragged
import hap.ruleengine.editor.viewmodel.event.MouseDragDropReleased
import hap.ruleengine.editor.viewmodel.parts.ComponentVM
import hap.ruleengine.editor.viewmodel.userinteraction.IUserInteraction
import hap.ruleengine.editor.viewmodel.userinteraction.UserInteractionFSM
import hap.ruleengine.parts.composite.CompositeComponent
import javafx.stage.Window

open class UserInteractionState constructor(val fsm: UserInteractionFSM) : EnterLeaveState(), IUserInteraction {
    override fun mouseReleased() {
        // Reset any action when user release the mouse button
        fsm.setState(NoAction(fsm))
    }

    override fun componentDragged(dragged: ComponentDragged) {
        // Nothing to do here
    }

    override fun selectComponent(component: ComponentVM, addToOrRemoveFromSelection: Boolean ) {
        // Nothing to do here
    }

    override fun openComposite(surface: DrawingSurfaceVM, window: Window) {
        fsm.setState(OpenComposite(fsm, surface, window))
    }

    override fun mouseDragDropReleased(event: MouseDragDropReleased, view: IDrawingSurfaceView, currentCC: CompositeComponent) {
        fsm.setState(NoAction(fsm))
    }

    override fun dragComponentFromComponentPallet(componentType: String) {
        fsm.setState(DraggingComponentFromPallet(componentType, fsm))
    }
}