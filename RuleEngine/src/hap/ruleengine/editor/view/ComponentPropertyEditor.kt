package hap.ruleengine.editor.view

import hap.ruleengine.editor.viewmodel.event.SelectedComponentsChanged
import tornadofx.*

class ComponentPropertyEditor : View("")
{
	override val root = borderpane {
		center {
			stackpane {
				subscribe<SelectedComponentsChanged> {
					replaceChildren {
						if (it.selectedComponents.size == 1)
						{
							val single = it.selectedComponents.values.first()

							form {
								fieldset("Basic Properties: ${single.component.javaClass.simpleName}")
								{
									field("Name") {
										textfield(single.name)
									}
								}
							}
						}
						else
						{
							text("Select single component to edit properties")
						}
					}
				}
			}
		}
	}
}
