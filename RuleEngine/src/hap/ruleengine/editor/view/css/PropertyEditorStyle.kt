package hap.ruleengine.editor.view.css

import javafx.geometry.Pos
import javafx.scene.layout.BackgroundPosition
import javafx.scene.layout.BackgroundRepeat
import tornadofx.Stylesheet
import tornadofx.cssclass
import tornadofx.multi
import java.net.URI

/**
 * Created by Per Malmberg on 2017-01-28.
 */

class PropertyEditorStyle : Stylesheet()
{
	companion object buttons
	{
		val revertButton by cssclass()
		val applyButton by cssclass()

	}

	init
	{
		PropertyEditorStyle.revertButton {
			backgroundImage = multi(URI("/famfamfam/icons/arrow_undo.png"))
			backgroundRepeat = multi(Pair(BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT))
			backgroundPosition = multi(BackgroundPosition.CENTER, BackgroundPosition.CENTER)
		}

		PropertyEditorStyle.applyButton {
			backgroundImage = multi(URI("/famfamfam/icons/accept.png"))
			backgroundRepeat = multi(Pair(BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT))
			backgroundPosition = multi(BackgroundPosition.CENTER, BackgroundPosition.CENTER)
		}
	}
}