package hap.ruleengine.editor.view

import hap.ruleengine.editor.viewmodel.event.OpenCompositeFromFile
import hap.ruleengine.editor.viewmodel.event.SaveComposite
import tornadofx.*


class RuleEngineTab : Fragment() {
    override val root = borderpane {
        top {
            // TODO: Can the menu of the window itself be set based on selected tab?
            menubar {
                menu("_File") {
                    menuitem("_Open Composite") {
                        fire(OpenCompositeFromFile(this@borderpane.scene.window))
                    }
                    separator()
                    menuitem("_Save") {
                        fire(SaveComposite(this@borderpane.scene.window))
                    }

                }
            }
        }
        center{
            splitpane {
                add(ComponentPalletView::class)
                add(DrawingSurfaceView::class)
                add(PropertyView::class)
            }
        }
    }

}
