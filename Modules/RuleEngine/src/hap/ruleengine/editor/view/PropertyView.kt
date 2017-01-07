package hap.ruleengine.editor.view

import hap.ruleengine.editor.viewmodel.event.SelectedComponentsChanged
import javafx.scene.control.ScrollPane
import tornadofx.*


class PropertyView : Fragment() {
    override val root = ScrollPane()
    var selectedComponentCount by property(0)
    fun selectedComponentCountProperty() = getProperty(PropertyView::selectedComponentCount)

    init {
        with( root ) {
            borderpane {
                top {
                    text("Property editor")
                }
                center {
                    vbox {
                        text("Selected count:")
                        text {
                            bind(selectedComponentCountProperty())
                        }
                    }
                }
            }


        }

        subscribe<SelectedComponentsChanged> {
            selectedComponentCount = it.selectedComponents.size
        }
    }
}
