package hap.ruleengine.editor.view

import hap.ruleengine.editor.viewmodel.event.NewComposite
import hap.ruleengine.editor.viewmodel.event.OpenCompositeFromFile
import hap.ruleengine.editor.viewmodel.event.SaveComposite
import hap.ruleengine.editor.viewmodel.event.SetLiveComponentsEvent
import tornadofx.*


class RuleEngineTab : Fragment()
{
	override val root = borderpane {
		top {
			// TODO: Can the menu of the window itself be set based on selected tab?
			menubar {
				menu("_File") {
					menuitem("_New Composite")
					{
						fire(NewComposite(this@borderpane.scene.window))
					}
					menuitem("_Open Composite") {
						fire(OpenCompositeFromFile(this@borderpane.scene.window))
					}
					separator()
					menuitem("_Save") {
						fire(SaveComposite(this@borderpane.scene.window))
					}
				}
				menu("_Options")
				{
					checkmenuitem("_Live components")
					{
						setOnAction {
							with(config) {
								fire(SetLiveComponentsEvent(isSelected))
							}
						}
					}
				}
			}
		}
		center {
			splitpane {
				add(ComponentPalletView::class)
				add(DrawingSurfaceView::class)
				add(PropertyView::class)
				setDividerPositions(0.3, 0.8, 0.9)
			}
		}
	}

}
