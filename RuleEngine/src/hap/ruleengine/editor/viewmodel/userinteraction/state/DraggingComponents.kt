package hap.ruleengine.editor.viewmodel.userinteraction.state

import hap.ruleengine.editor.viewmodel.event.ComponentDragged
import hap.ruleengine.editor.viewmodel.parts.ComponentVM
import hap.ruleengine.editor.viewmodel.userinteraction.UserInteractionFSM

class DraggingComponents constructor(fsm: UserInteractionFSM) : UserInteractionState(fsm) {
    var dragStarted = false

    override fun mouseReleased() {
        // Don't reset on mouse-release. Instead, do it when the next selection event happens.
    }

    override fun selectComponent(component: ComponentVM, addToOrRemoveFromSelection: Boolean) {
        // Reset any action when user releases the mouse button
        fsm.setState(NoAction(fsm))
    }

    override fun componentDragged(dragged: ComponentDragged) {
        val mouseEvent = dragged.event

        if (!dragStarted) {
            dragStarted = true
            fsm.getSelectedComponents().forEach {
                it.startDragComponent(mouseEvent.sceneX, mouseEvent.sceneY, fsm.surface)
            }
        }

        fsm.getSelectedComponents().forEach {
            it.moveFromOriginalPosition(mouseEvent.sceneX, mouseEvent.sceneY, fsm.surface)
        }
    }
}