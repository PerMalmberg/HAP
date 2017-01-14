package hap.ruleengine.editor.view

import hap.ruleengine.component.placeholder.PlaceHolderComponent
import hap.ruleengine.editor.viewmodel.event.SelectedComponentsChanged
import hap.ruleengine.editor.viewmodel.parts.ComponentVM
import tornadofx.*
import java.util.*

class BasicPropertyEditor : View("") {
    var selectedComponents: HashMap<UUID, ComponentVM> by property(HashMap<UUID, ComponentVM>())
    val dummy = HashMap<UUID, ComponentVM>()

    fun selectedComponentsProperty() = getProperty(BasicPropertyEditor::selectedComponents)

    override val root = borderpane {
        center {
            stackpane {

            }.apply {
                selectedComponentsProperty().addListener { observableValue, oldValue, newValue ->
                    this.replaceChildren {
                        if (selectedComponents.size == 1) {
                            val single = selectedComponents.values.first()

                            form {
                                fieldset("Basic Properties: ${single.component.javaClass.simpleName}")
                                {
                                    field("Name") {
                                        textfield(single.name)
                                    }
                                }

                                hbox {
                                    button("Save") {
                                        disableProperty().bind(single.dirtyStateProperty().not())
                                        setOnAction {
                                            single.commit()
                                        }
                                    }

                                    button("Cancel") {
                                        disableProperty().bind(single.dirtyStateProperty().not())
                                        setOnAction {
                                            single.rollback()
                                        }
                                    }
                                }
                            }
                        } else {
                            text("Select single component to edit properties")
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
