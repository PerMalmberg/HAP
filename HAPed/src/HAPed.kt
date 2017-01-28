import hap.ruleengine.editor.view.css.ComponentStyle
import hap.ruleengine.editor.view.css.PropertyEditorStyle
import tornadofx.App
import tornadofx.importStylesheet
import view.MainView

class HAPed : App(MainView::class)
{
	init
	{
		importStylesheet(ComponentStyle::class)
		importStylesheet(PropertyEditorStyle::class)
	}
}