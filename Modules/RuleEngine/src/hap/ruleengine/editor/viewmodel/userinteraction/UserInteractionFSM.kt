package hap.ruleengine.editor.viewmodel.userinteraction

import hap.ruleengine.editor.view.parts.WireView
import hap.ruleengine.editor.viewmodel.DrawingSurfaceVM
import hap.ruleengine.editor.viewmodel.IDrawingSurfaceView
import hap.ruleengine.editor.viewmodel.event.ComponentDragged
import hap.ruleengine.editor.viewmodel.event.MouseDragDropReleased
import hap.ruleengine.editor.viewmodel.parts.ComponentVM
import hap.ruleengine.editor.viewmodel.userinteraction.state.ConnectingWire
import hap.ruleengine.editor.viewmodel.userinteraction.state.NoAction
import hap.ruleengine.editor.viewmodel.userinteraction.state.UserInteractionState
import hap.ruleengine.parts.IConnectionPoint
import hap.ruleengine.parts.composite.CompositeComponent
import javafx.geometry.Point2D
import javafx.stage.Window
import java.io.File
import java.util.*

class UserInteractionFSM(val surface: IDrawingSurfaceView) : chainedfsm.FSM<UserInteractionState>(), IUserInteraction {
    override fun deleteWire(wire: WireView) {
        currentState.deleteWire(wire)
    }

    override fun updateDragWire(sceneX: Double, sceneY: Double) {
        currentState.updateDragWire(sceneX, sceneY)
    }

    override fun mouseEnteredConnectionPoint(connectionPoint: IConnectionPoint?) {
        currentState.mouseEnteredConnectionPoint(connectionPoint)
    }

    override fun beginConnectWire(connectionPoint: IConnectionPoint, sceneRelativeCenter: Point2D) {
        setState(ConnectingWire(this, connectionPoint, surface.getVM(), sceneRelativeCenter))
    }

    fun getSelectedComponents() = selectedComponents.values.toList()


    override fun mouseReleased() {
        currentState.mouseReleased()
    }

    override fun componentDragged(dragged: ComponentDragged) {
        currentState.componentDragged(dragged)
    }

    override fun selectComponent(component: ComponentVM, addToOrRemoveFromSelection: Boolean) {
        currentState.selectComponent(component, addToOrRemoveFromSelection)
    }

    override fun openComposite(surface: DrawingSurfaceVM, window: Window) {
        currentState.openComposite(surface, window)
    }

    override fun saveComposite(surface: DrawingSurfaceVM, window: Window) {
        currentState.saveComposite(surface, window)
    }


    override fun mouseDragDropReleased(event: MouseDragDropReleased, view: IDrawingSurfaceView, currentCC: CompositeComponent) {
        currentState.mouseDragDropReleased(event, view, currentCC)
    }

    override fun dragComponentFromComponentPallet(componentType: String) {
        currentState.dragComponentFromComponentPallet(componentType)
    }

    init {
        setState(NoAction(this))
    }

    var currentFile: File? = null
    val selectedComponents = HashMap<UUID, ComponentVM>()
}
