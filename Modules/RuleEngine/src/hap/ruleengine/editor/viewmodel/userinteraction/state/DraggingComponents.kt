package hap.ruleengine.editor.viewmodel.userinteraction.state

import hap.ruleengine.editor.viewmodel.event.ComponentDragged
import hap.ruleengine.editor.viewmodel.userinteraction.UserInteractionFSM

class DraggingComponents constructor(fsm: UserInteractionFSM) : UserInteractionState(fsm) {
    var dragStarted = false

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