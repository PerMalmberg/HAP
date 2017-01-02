package hap.ruleengine.editor.viewmodel

import hap.ruleengine.parts.IConnectionPoint
import javafx.scene.paint.Color
import tornadofx.Controller
import tornadofx.ViewModel
import tornadofx.observable


class InputVM(val index: Int, val color: Color, val connectionPoint: IConnectionPoint) : ViewModel() {
    val name = bind { connectionPoint.observable(IConnectionPoint::getName, IConnectionPoint::setName) }
}