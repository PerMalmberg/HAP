package hap.ruleengine.editor.viewmodel.userinteraction

import hap.ruleengine.editor.viewmodel.DrawingSurfaceVM
import hap.ruleengine.editor.viewmodel.IDrawingSurfaceView
import hap.ruleengine.editor.viewmodel.Publisher
import hap.ruleengine.editor.viewmodel.event.ComponentDragged
import hap.ruleengine.editor.viewmodel.event.MouseDragDropReleased
import hap.ruleengine.editor.viewmodel.event.SelectedComponentsChanged
import hap.ruleengine.editor.viewmodel.parts.ComponentVM
import hap.ruleengine.editor.viewmodel.userinteraction.state.DraggingComponents
import hap.ruleengine.editor.viewmodel.userinteraction.state.NoAction
import hap.ruleengine.editor.viewmodel.userinteraction.state.UserInteractionState
import hap.ruleengine.parts.composite.CompositeComponent
import javafx.stage.Window
import java.util.*

class UserInteractionFSM(val surface: IDrawingSurfaceView) : chainedfsm.FSM<UserInteractionState>(), IUserInteraction {
    private val selectedComponents = HashMap<UUID, ComponentVM>()
    var skipNextSelectionEvent: Boolean = false

    private val pub = Publisher()

    init {
        setState(NoAction(this))
    }

    fun getSelectedComponents() = selectedComponents.values.toList()

    override fun mouseReleased() {
        // Reset any action when user releases the mouse button
        setState(NoAction(this))
    }

    override fun componentDragged(dragged: ComponentDragged) {
        currentState.componentDragged(dragged)
    }

    override fun selectComponent(component: ComponentVM, addToOrRemoveFromSelection: Boolean ) {
        if( !skipNextSelectionEvent) {
            val deselected = ArrayList<ComponentVM>()

            if (addToOrRemoveFromSelection) {
                setSelectionState(component, !component.isSelected)
            } else {
                // Only the clicked component should remain selected
                selectedComponents.values.forEach {
                    it.isSelected = false
                    deselected.add(it)
                }
                selectedComponents.clear()

                setSelectionState(component, true)
            }

            // Let those listening for changes in selected components know of the new selection state
            pub.fire(SelectedComponentsChanged(selectedComponents, deselected))
        }

        skipNextSelectionEvent = false
    }

    private fun setSelectionState( component : ComponentVM, isSelected:Boolean)
    {
        // Select/deselect component
        component.isSelected = isSelected

        if (component.isSelected) {
            selectedComponents.put(component.component.id, component)
        } else {
            selectedComponents.remove(component.component.id)
        }
    }

    override fun openComposite(surface: DrawingSurfaceVM, window: Window) {
        currentState.openComposite(surface, window)
    }


    override fun mouseDragDropReleased(event: MouseDragDropReleased, view: IDrawingSurfaceView, currentCC: CompositeComponent) {
        currentState.mouseDragDropReleased(event, view, currentCC)
    }

    override fun dragComponentFromComponentPallet(componentType: String) {
        currentState.dragComponentFromComponentPallet(componentType)
    }
}