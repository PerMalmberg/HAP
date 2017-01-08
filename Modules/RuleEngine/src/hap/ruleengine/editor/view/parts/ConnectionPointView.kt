package hap.ruleengine.editor.view.parts

import hap.ruleengine.editor.viewmodel.event.BeginConnectWire
import hap.ruleengine.editor.viewmodel.event.MouseEnteredConnectionPoint
import hap.ruleengine.parts.IConnectionPoint
import javafx.geometry.Point2D
import javafx.scene.shape.Rectangle
import tornadofx.Fragment
import tornadofx.singleAssign
import java.util.*

abstract class ConnectionPointView : Fragment() {
    protected val myWire: ArrayList<WireView> = ArrayList()
    protected var myConnectionPoint: Rectangle by singleAssign()

    fun addWire(wire: WireView) {
        myWire.add(wire)
    }

    fun hasWires() = myWire.count() > 0

    fun getSceneRelativeCenter(): Point2D {
        val bounds = myConnectionPoint.boundsInParent
        val offset = connectionPointSize / 2
        return myConnectionPoint.localToScene(bounds.minX + offset, bounds.minY + offset)
    }

    fun updateWires() {
        myWire.forEach { it.updateEndPoints() }
    }

    protected fun startConnectWire(connectionPoint: IConnectionPoint) {
        fire(BeginConnectWire(connectionPoint))
    }

    protected fun enteredConnectionPoint(connectionPoint: IConnectionPoint?) {
        fire(MouseEnteredConnectionPoint(connectionPoint))
    }
}

const val connectionPointSize: Double = 8.0
