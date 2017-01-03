package hap.ruleengine.editor.viewmodel

import hap.ruleengine.editor.viewmodel.parts.ComponentVM
import javafx.geometry.Point2D

interface IDrawingSurfaceView {
    fun clearComponents()
    fun add(vm: ComponentVM)
    fun sceneToLocal(sceneX: Double, sceneY: Double): Point2D
    fun drawWires()
}