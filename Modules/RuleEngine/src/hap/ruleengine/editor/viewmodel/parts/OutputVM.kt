package hap.ruleengine.editor.viewmodel.parts

import hap.ruleengine.parts.IConnectionPoint
import javafx.scene.paint.Color
import tornadofx.ViewModel
import tornadofx.observable


class OutputVM(val index: Int, val color: Color, val connectionPoint: IConnectionPoint) : ViewModel() {
    val name = bind { connectionPoint.observable(IConnectionPoint::getName, IConnectionPoint::setName) }
}