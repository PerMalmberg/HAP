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
            stroke = Color.RED
        }
    }

    private fun setStart(sceneCoordinates: Point2D) {
        val parentLocal = parent.sceneToLocal(sceneCoordinates)
        startX = parentLocal.x
        startY = parentLocal.y
    }

    private fun setEnd(sceneCoordinates: Point2D) {
        val parentLocal = parent.sceneToLocal(sceneCoordinates)
        endX = parentLocal.x
        endY = parentLocal.y
    }

    fun updateEndPoints() {
        val sourceCenter = source.getSceneRelativeCenter()
        setStart(sourceCenter)
        val targetCenter = target.getSceneRelativeCenter()
        setEnd(targetCenter)
    }
}