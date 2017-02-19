package hap.ruleengine.editor.view

import tornadofx.Fragment
import tornadofx.stackpane
import tornadofx.vbox


class PropertyView : Fragment()
{
	override val root = stackpane {
		vbox {
			add(ComponentPropertyEditor::class)
			add(PropertyEditorView::class)
			add(DebugView::class)
		}
	}
}
