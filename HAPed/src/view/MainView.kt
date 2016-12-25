package view

import javafx.scene.layout.BorderPane
import tornadofx.*
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
            left(ComponentView::class)
            center {
                canvas {

                }
            }
            right(PropertyView::class)

        }
    }
}
