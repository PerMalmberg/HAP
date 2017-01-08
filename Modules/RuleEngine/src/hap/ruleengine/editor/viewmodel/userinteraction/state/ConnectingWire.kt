package hap.ruleengine.editor.viewmodel.userinteraction.state

import hap.ruleengine.editor.viewmodel.DrawingSurfaceVM
import hap.ruleengine.editor.viewmodel.IDrawingSurfaceView
import hap.ruleengine.editor.viewmodel.event.MouseDragDropReleased
import hap.ruleengine.editor.viewmodel.userinteraction.UserInteractionFSM
import hap.ruleengine.parts.IConnectionPoint
import hap.ruleengine.parts.composite.CompositeComponent


class ConnectingWire constructor(fsm: UserInteractionFSM, val startPoint: IConnectionPoint, val drawingVM: DrawingSurfaceVM) : UserInteractionState(fsm) {
    var lastEntered : IConnectionPoint? = null

    override fun mouseEnteredConnectionPoint(connectionPoint: IConnectionPoint?) {
        lastEntered = connectionPoint
    }

    override fun mouseDragDropReleased(event: MouseDragDropReleased, view: IDrawingSurfaceView, currentCC: CompositeComponent) {
        // If we still have a 'lastEntered', we should try to connect the to points
        if( lastEntered != null ) {
            drawingVM.addWire( startPoint, lastEntered as IConnectionPoint)
        }

        fsm.setState(NoAction(fsm))
    }

}