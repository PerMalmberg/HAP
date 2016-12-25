package hap.ruleengine.editor.view

import hap.ruleengine.editor.view.ComponentPalletView
import javafx.scene.layout.BorderPane
import tornadofx.*
import tornadofx.canvas
import tornadofx.center
import tornadofx.menu
import tornadofx.menubar
import tornadofx.menuitem
import tornadofx.top
import kotlin.system.exitProcess


class MainView : View("My View") {
    override val root = BorderPane()

    init {
        with(root) {
            this.minWidth = 800.0
            this.minHeight = 800.0

            top {
                menubar {
                    menu("_File") {
                        menuitem("_Open") { }
                        menuitem("E_xit") { exitProcess(0) }
                    }
                }
            }
            left(ComponentPalletView::class)
            center {
                canvas {

                }
            }
            right(PropertyView::class)

        }
    }
}
