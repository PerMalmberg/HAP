package hap.ruleengine.editor.viewmodel.userinteraction.state

import chainedfsm.LeaveChain
import chainedfsm.interfaces.ILeave
import hap.ruleengine.editor.viewmodel.DrawingSurfaceVM
import hap.ruleengine.editor.viewmodel.IDrawingSurfaceView
import hap.ruleengine.editor.viewmodel.event.MouseDragDropReleased
import hap.ruleengine.editor.viewmodel.userinteraction.UserInteractionFSM
import hap.ruleengine.parts.IConnectionPoint
import hap.ruleengine.parts.composite.CompositeComponent
import javafx.geometry.Point2D


class ConnectingWire (fsm: UserInteractionFSM, val startPoint: IConnectionPoint, val drawingVM: DrawingSurfaceVM, val startSceneRelativeCenter: Point2D) : UserInteractionState(fsm) {
    var lastEntered : IConnectionPoint? = null

    init {
        LeaveChain(this, ILeave { drawingVM.hideDragWire() })
    }

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

    override fun updateDragWire(sceneX: Double, sceneY: Double) {
        drawingVM.setDragWire( startSceneRelativeCenter, sceneX, sceneY)
    }

}