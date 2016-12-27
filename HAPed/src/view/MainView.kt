package view

import hap.ruleengine.editor.view.ComponentPalletView
import hap.ruleengine.editor.view.DrawingSurface
import hap.ruleengine.editor.view.PropertyView
import javafx.scene.layout.BorderPane
import tornadofx.*
import kotlin.system.exitProcess


class MainView : View("My View") {
    override val root = BorderPane()

    init {


        with(root) {

            top {
                menubar {
                    menu("_File") {
                        menuitem("_Open") { }
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
