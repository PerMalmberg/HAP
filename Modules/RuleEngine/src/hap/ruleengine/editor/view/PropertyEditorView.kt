package hap.ruleengine.editor.view

import hap.ruleengine.component.placeholder.PlaceHolderComponent
import hap.ruleengine.editor.viewmodel.ViewBuilder
import hap.ruleengine.editor.viewmodel.event.SelectedComponentsChanged
import hap.ruleengine.editor.viewmodel.parts.ComponentVM
import javafx.scene.layout.StackPane
import tornadofx.*
import java.util.*

class PropertyEditorView : Fragment() {
    var selectedComponents: HashMap<UUID, ComponentVM> by property(HashMap<UUID, ComponentVM>())
    fun selectedComponentsProperty() = getProperty(ComponentPropertyEditor::selectedComponents)
    val dummy = HashMap<UUID, ComponentVM>()

    override val root = borderpane {
        center {
            stackpane {

            }.apply {
                selectedComponentsProperty().addListener { observableValue, oldValue, newValue ->
                    this.replaceChildren {
                        if (selectedComponents.size == 1) {
                            val single = selectedComponents.values.first()
                            val vb = ViewBuilder( this )
                            single.component.showProperties(vb)

                        }
                    }
                }
            }
        }
    }

    init {
        // The selectedComponentsProperty() change listener doesn't fire if the new hash map is empty or if the
        // same map is assigned so we need an intermediate map.
        dummy.put(UUID.randomUUID(), ComponentVM(PlaceHolderComponent()))

        subscribe<SelectedComponentsChanged> {
            selectedComponents = dummy
            selectedComponents = it.selectedComponents
        }
    }
}

