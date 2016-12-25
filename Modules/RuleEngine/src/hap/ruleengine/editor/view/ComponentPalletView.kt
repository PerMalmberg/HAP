package hap.ruleengine.editor.view

import javafx.scene.control.ScrollPane
import tornadofx.Fragment
import tornadofx.scrollpane
import tornadofx.text


class ComponentPalletView : Fragment() {
    override val root = ScrollPane()



    init {
        with(root)
        {
            text("Available symbols")
        }
    }
}
