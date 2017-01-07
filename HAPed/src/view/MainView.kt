package view

import hap.ruleengine.editor.view.ComponentPalletView
import hap.ruleengine.editor.view.DrawingSurface
import hap.ruleengine.editor.view.PropertyView
import hap.ruleengine.editor.viewmodel.event.OpenCompositeFromFile
import javafx.event.Event
import javafx.scene.control.TabPane
import tornadofx.*


class MainView : View("HAPed") {
    override val root = TabPane()

    init {
        with(root) {
            // TODO: Let user open/close tabs based on menu selection in main widow
            tab("Rule editor") {
                setOnCloseRequest(Event::consume) // Don't let the user close the tab
                borderpane {
                    top {
                        // TODO: Can the menu of the window itself be set based on selected tab?
                        menubar {
                            menu("_File") {
                                menuitem("_Open Composite") {
                                    fire(OpenCompositeFromFile(root.scene.window))
                                }
                            }
                        }
                    }
                    center{
                        splitpane {
                            add(ComponentPalletView::class)
                            add(DrawingSurface::class)
                            add(PropertyView::class)
                        }
                    }
                }
            }
            tab("Other module configuration")
            {

            }
        }
    }
}
