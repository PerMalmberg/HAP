package hap.ruleengine.editor.view.parts

import javafx.geometry.Point2D
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import tornadofx.px
import tornadofx.style

class WireView(val source: OutputView, val target: InputView) : Line() {
    init {
        source.addWire(this)
        target.addWire(this)

        style {
            strokeWidth = 2.px
            stroke = source.color
        }
    }

    private fun setStart(sceneCoordinates: Point2D) {
        val local = parent.sceneToLocal(sceneCoordinates)
        startX = local.x
        startY = local.y
    }

    private fun setEnd(sceneCoordinates: Point2D) {
        val local = parent.sceneToLocal(sceneCoordinates)
        endX = local.x
        endY = local.y
    }

    fun updateEndPoints() {
        val sourceCenter = source.getSceneRelativeCenter()
        setStart(sourceCenter)
        val targetCenter = target.getSceneRelativeCenter()
        setEnd(targetCenter)
    }
}