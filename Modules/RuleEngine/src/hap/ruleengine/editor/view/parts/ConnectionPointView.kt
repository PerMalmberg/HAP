package hap.ruleengine.editor.view.parts

import javafx.geometry.Point2D
import javafx.scene.shape.Rectangle
import tornadofx.Fragment
import tornadofx.singleAssign
import java.util.*

abstract class ConnectionPointView : Fragment() {
    protected val myWire: ArrayList<WireView> = ArrayList()
    protected var myConnectionPoint : Rectangle by singleAssign()

    fun addWire(wire: WireView) {
        myWire.add(wire)
    }

    fun getSceneRelativeCenter(): Point2D
    {
        val bounds = myConnectionPoint.boundsInParent
        return myConnectionPoint.localToScene( bounds.minX, bounds.minY )
    }
}

const val connectionPointSize: Double = 6.0
