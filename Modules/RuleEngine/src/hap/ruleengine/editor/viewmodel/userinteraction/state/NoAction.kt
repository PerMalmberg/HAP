package hap.ruleengine.editor.viewmodel.userinteraction.state

import hap.ruleengine.editor.view.parts.WireView
import hap.ruleengine.editor.viewmodel.event.ComponentDragged
import hap.ruleengine.editor.viewmodel.userinteraction.UserInteractionFSM

class NoAction constructor(fsm: UserInteractionFSM) : UserInteractionState(fsm) {

    override fun deleteWire(wire: WireView) {
        fsm.surface.removeWire( wire )
    }

    override fun componentDragged(dragged: ComponentDragged) {
        fsm.setState(DraggingComponents(fsm))
    }
}