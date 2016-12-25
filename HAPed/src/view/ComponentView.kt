package view

import javafx.scene.control.ScrollPane
import tornadofx.Fragment
import tornadofx.scrollpane
import tornadofx.text


class ComponentView : Fragment() {
    override val root = ScrollPane()

    init {
        with(root)
        {
            text("Available symbols")
        }
    }
}
