package view

import hap.ruleengine.editor.view.ComponentPalletView
import hap.ruleengine.editor.view.DrawingSurface
import hap.ruleengine.editor.view.PropertyView
import hap.ruleengine.editor.viewmodel.event.OpenCompositeFromFile
import javafx.scene.layout.BorderPane
import tornadofx.*
import kotlin.system.exitProcess


class MainView : View("HAPed") {
    override val root = BorderPane()

    init {

        with(root) {

            top {
                menubar {
                    menu("_File") {
                        menuitem("_Open Composite") {
                            fire(OpenCompositeFromFile( root.scene.window ))
                        }
                        separator()
                        menuitem("E_xit") { exitProcess(0) }
                    }
                }
            }
            left(ComponentPalletView::class)
            center(DrawingSurface::class)
            right(PropertyView::class)
        }
    }
}
