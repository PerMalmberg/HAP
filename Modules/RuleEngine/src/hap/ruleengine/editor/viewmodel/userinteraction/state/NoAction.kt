package hap.ruleengine.editor.viewmodel.userinteraction.state

import hap.ruleengine.editor.view.parts.ComponentView
import hap.ruleengine.editor.view.parts.WireView
import hap.ruleengine.editor.viewmodel.event.ComponentDragged
import hap.ruleengine.editor.viewmodel.parts.ComponentVM
import hap.ruleengine.editor.viewmodel.userinteraction.UserInteractionFSM

class NoAction constructor(fsm: UserInteractionFSM) : UserInteractionState(fsm) {

    override fun deleteWire(wire: WireView) {
        fsm.surface.delete( wire )
    }

    override fun componentDragged(dragged: ComponentDragged) {
        fsm.setState(DraggingComponents(fsm))
    }

    override fun deleteComponent(component: ComponentView) {
        deselectComponent(component.vm)
        fsm.surface.deleteComponent(component)
    }
}