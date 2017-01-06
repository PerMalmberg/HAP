package hap.ruleengine.editor.viewmodel.userinteraction

import hap.ruleengine.editor.viewmodel.DrawingSurfaceVM
import hap.ruleengine.editor.viewmodel.IDrawingSurfaceView
import hap.ruleengine.editor.viewmodel.event.MouseDragReleased
import hap.ruleengine.editor.viewmodel.userinteraction.IUserInteraction
import hap.ruleengine.editor.viewmodel.userinteraction.state.NoAction
import hap.ruleengine.editor.viewmodel.userinteraction.state.UserInteractionState
import hap.ruleengine.parts.composite.CompositeComponent
import javafx.stage.Window

class UserInteractionFSM : chainedfsm.FSM<UserInteractionState>(), IUserInteraction {
    override fun openComposite(surface: DrawingSurfaceVM, window: Window) {
        currentState.openComposite(surface, window)
    }

    override fun mouseDragReleased(event: MouseDragReleased, view: IDrawingSurfaceView, currentCC: CompositeComponent) {
        currentState.mouseDragReleased(event, view, currentCC)
    }

    override fun dragComponentFromComponentPallet(componentType: String) {
        currentState.dragComponentFromComponentPallet(componentType)
    }


    init {
        setState(NoAction(this))
    }
}