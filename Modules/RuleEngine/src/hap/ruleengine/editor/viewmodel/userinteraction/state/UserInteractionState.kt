package hap.ruleengine.editor.viewmodel.userinteraction.state

import chainedfsm.EnterLeaveState
import hap.ruleengine.editor.viewmodel.DrawingSurfaceVM
import hap.ruleengine.editor.viewmodel.IDrawingSurfaceView
import hap.ruleengine.editor.viewmodel.Publisher
import hap.ruleengine.editor.viewmodel.event.ComponentDragged
import hap.ruleengine.editor.viewmodel.event.MouseDragDropReleased
import hap.ruleengine.editor.viewmodel.event.SelectedComponentsChanged
import hap.ruleengine.editor.viewmodel.parts.ComponentVM
import hap.ruleengine.editor.viewmodel.userinteraction.IUserInteraction
import hap.ruleengine.editor.viewmodel.userinteraction.UserInteractionFSM
import hap.ruleengine.parts.composite.CompositeComponent
import javafx.stage.Window
import java.util.*

open class UserInteractionState constructor(val fsm: UserInteractionFSM) : EnterLeaveState(), IUserInteraction {
    private val pub = Publisher()

    override fun mouseReleased() {
        // Reset any action when user release the mouse button
        fsm.setState(NoAction(fsm))
    }

    override fun componentDragged(dragged: ComponentDragged) {
        // Nothing to do here
    }

    override fun selectComponent(component: ComponentVM, addToOrRemoveFromSelection: Boolean ) {
        val deselected = ArrayList<ComponentVM>()

        if (addToOrRemoveFromSelection) {
            setSelectionState(component, !component.isSelected)
        } else {
            // Only the clicked component should remain selected
            fsm.selectedComponents.values.forEach {
                it.isSelected = false
                deselected.add(it)
            }
            fsm.selectedComponents.clear()

            setSelectionState(component, true)
        }

        // Let those listening for changes in selected components know of the new selection state
        pub.fire(SelectedComponentsChanged(fsm.selectedComponents, deselected))
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

    private fun setSelectionState(component: ComponentVM, isSelected: Boolean) {
        // Select/deselect component
        component.isSelected = isSelected

        if (component.isSelected) {
            fsm.selectedComponents.put(component.component.id, component)
        } else {
            fsm.selectedComponents.remove(component.component.id)
        }
    }
}