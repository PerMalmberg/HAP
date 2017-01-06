package hap.ruleengine.editor.viewmodel.userinteraction

import hap.ruleengine.editor.viewmodel.DrawingSurfaceVM
import hap.ruleengine.editor.viewmodel.IDrawingSurfaceView
import hap.ruleengine.editor.viewmodel.event.MouseDragReleased
import hap.ruleengine.parts.composite.CompositeComponent
import javafx.stage.Window

interface IUserInteraction {
    fun dragComponentFromComponentPallet(componentType: String)
    fun mouseDragReleased(event: MouseDragReleased, view: IDrawingSurfaceView, currentCC: CompositeComponent)
    fun openComposite(surface: DrawingSurfaceVM, window: Window)
}