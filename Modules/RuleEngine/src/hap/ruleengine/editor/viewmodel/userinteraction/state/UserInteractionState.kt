package hap.ruleengine.editor.viewmodel.userinteraction.state

import chainedfsm.EnterLeaveState
import chainedfsm.FSM
import hap.ruleengine.editor.viewmodel.DrawingSurfaceVM
import hap.ruleengine.editor.viewmodel.IDrawingSurfaceView
import hap.ruleengine.editor.viewmodel.event.MouseDragReleased
import hap.ruleengine.editor.viewmodel.userinteraction.IUserInteraction
import hap.ruleengine.parts.composite.CompositeComponent
import javafx.stage.Window

open class UserInteractionState constructor(val myFsm: FSM<UserInteractionState>) : EnterLeaveState(), IUserInteraction {
    override fun openComposite(surface: DrawingSurfaceVM, window: Window) {
        myFsm.setState(OpenComposite(myFsm, surface, window))
    }

    override fun mouseDragReleased(event: MouseDragReleased, view: IDrawingSurfaceView, currentCC: CompositeComponent) {}

    override fun dragComponentFromComponentPallet(componentType: String) {
        myFsm.setState(DraggingComponentFromPallet(componentType, myFsm))
    }
}