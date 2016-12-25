package view

import javafx.scene.control.ScrollPane
import tornadofx.Fragment
import tornadofx.text


class PropertyView : Fragment() {
    override val root = ScrollPane()

    init {
        with( root ) {
            text("Property editor")
        }
    }
}
