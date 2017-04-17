package hap.ruleengine.editor.viewmodel

import hap.ruleengine.editor.view.parts.ComponentView
import hap.ruleengine.editor.view.parts.WireView
import hap.ruleengine.editor.viewmodel.parts.ComponentVM
import hap.ruleengine.parts.composite.CompositeComponent
import javafx.geometry.Point2D

interface IDrawingSurfaceView
{
	fun clearComponents()
	fun add(vm: ComponentVM)
	fun sceneToLocal(sceneX: Double, sceneY: Double): Point2D
	fun drawWires(cc: CompositeComponent)
	fun getVM(): DrawingSurfaceVM
	fun delete(wire: WireView)
	fun deleteComponent(component: ComponentView)
	fun getScrollOffset(): Point2D
}