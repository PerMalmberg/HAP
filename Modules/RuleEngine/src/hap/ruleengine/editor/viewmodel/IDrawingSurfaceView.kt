package hap.ruleengine.editor.viewmodel

import hap.ruleengine.editor.view.parts.ComponentView
import javafx.geometry.Point2D

interface IDrawingSurfaceView {
    fun clearComponents()
    fun add(cv: ComponentView)
    fun sceneToLocal(sceneX: Double, sceneY: Double): Point2D
    fun drawWires()
}