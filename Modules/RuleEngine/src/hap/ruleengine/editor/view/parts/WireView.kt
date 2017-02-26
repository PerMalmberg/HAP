package hap.ruleengine.editor.view.parts

import hap.ruleengine.editor.viewmodel.event.DeleteWire
import hap.ruleengine.parts.Wire.IWire
import javafx.geometry.Point2D
import javafx.scene.input.MouseButton
import javafx.scene.shape.Line
import tornadofx.FX
import tornadofx.px
import tornadofx.removeFromParent
import tornadofx.style

class WireView(val source: OutputView, val target: InputView) : Line()
{

	init
	{
		source.addWire(this)
		target.addWire(this)

		style {
			strokeWidth = 2.px
			stroke = source.color
		}

		setOnMouseClicked {
			if (it.button == MouseButton.SECONDARY)
			{
				FX.eventbus.fire(DeleteWire(this))
			}
		}

		setOnMouseEntered {
			strokeWidth *= 2
		}

		setOnMouseExited {
			strokeWidth /= 2
		}
	}

	fun delete()
	{
		removeFromParent()
		source.disconnect(this)
		target.disconnect(this)
	}

	private fun setStart(sceneCoordinates: Point2D)
	{
		val local = parent.sceneToLocal(sceneCoordinates)
		startX = local.x
		startY = local.y
	}

	private fun setEnd(sceneCoordinates: Point2D)
	{
		val local = parent.sceneToLocal(sceneCoordinates)
		endX = local.x
		endY = local.y
	}

	fun updateEndPoints()
	{
		val sourceCenter = source.getSceneRelativeCenter()
		setStart(sourceCenter)
		val targetCenter = target.getSceneRelativeCenter()
		setEnd(targetCenter)
	}

	var wire: IWire? = null
}