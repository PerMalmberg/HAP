package hap.ruleengine.editor.viewmodel.userinteraction

import hap.ruleengine.editor.view.parts.WireView
import hap.ruleengine.editor.viewmodel.DrawingSurfaceVM
import hap.ruleengine.editor.viewmodel.IDrawingSurfaceView
import hap.ruleengine.editor.viewmodel.event.ComponentDragged
import hap.ruleengine.editor.viewmodel.event.MouseDragDropReleased
import hap.ruleengine.editor.viewmodel.parts.ComponentVM
import hap.ruleengine.parts.IConnectionPoint
import hap.ruleengine.parts.composite.CompositeComponent
import javafx.geometry.Point2D
import javafx.stage.Window

interface IUserInteraction {
    fun dragComponentFromComponentPallet(componentType: String)
    fun mouseDragDropReleased(event: MouseDragDropReleased, view: IDrawingSurfaceView, currentCC: CompositeComponent)
    fun openComposite(surface: DrawingSurfaceVM, window: Window)
    fun selectComponent(component: ComponentVM, addToOrRemoveFromSelection: Boolean)
    fun componentDragged(dragged: ComponentDragged)
    fun mouseReleased()
    fun saveComposite(surface: DrawingSurfaceVM, window: Window)
    fun beginConnectWire(connectionPoint: IConnectionPoint, sceneRelativeCenter: Point2D)
    fun mouseEnteredConnectionPoint(connectionPoint: IConnectionPoint?)
    fun updateDragWire(sceneX: Double, sceneY: Double)
    fun deleteWire(wire: WireView)
}