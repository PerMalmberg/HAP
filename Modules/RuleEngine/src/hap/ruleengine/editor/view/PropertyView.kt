package hap.ruleengine.editor.view

import javafx.scene.control.ScrollPane
import tornadofx.*


class PropertyView : Fragment() {
    override val root = ScrollPane()
    var selectedComponentCount: Int by property(0)

    fun selectedComponentCountProperty() = getProperty(PropertyView::selectedComponentCount)

    init {
        with(root) {
            borderpane {
                top {
                    text {
                        selectedComponentCountProperty().addListener { observableValue, oldValue, newValue ->
                            this.text = "Selected components: $newValue"
                        }
                    }
                }
                center {
                    this += (BasicPropertyEditor::class)
                }
            }
        }
    }
}
