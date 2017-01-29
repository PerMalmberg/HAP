import hap.ruleengine.editor.view.css.ComponentStyle
import hap.ruleengine.editor.view.css.PalletStyle
import hap.ruleengine.editor.view.css.PropertyEditorStyle
import javafx.scene.Scene
import tornadofx.App
import tornadofx.UIComponent
import tornadofx.importStylesheet
import view.MainView

class HAPed : App(MainView::class)
{
	init
	{
		importStylesheet(ComponentStyle::class)
		importStylesheet(PropertyEditorStyle::class)
		importStylesheet(PalletStyle::class)
	}

	override fun createPrimaryScene(view: UIComponent): Scene
	{
		return Scene(view.root, 800.0, 600.0)
	}
}