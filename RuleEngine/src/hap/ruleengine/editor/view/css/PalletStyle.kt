package hap.ruleengine.editor.view.css

import javafx.scene.paint.Color
import tornadofx.Stylesheet
import tornadofx.cssclass
import tornadofx.derive
import tornadofx.multi

/**
 * Created by Per Malmberg on 2017-01-28.
 */

class PalletStyle : Stylesheet()
{
	companion object buttons
	{
		val mouseOver by cssclass()
		val componentRowStyle by cssclass()
	}

	init
	{
		PalletStyle.mouseOver {
			backgroundColor = multi(Color.LIGHTGRAY)
		}

		PalletStyle.componentRowStyle {
			backgroundColor = multi(Color.GRAY)
		}
	}
}