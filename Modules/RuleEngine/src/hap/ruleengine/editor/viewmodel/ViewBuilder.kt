package hap.ruleengine.editor.viewmodel

import hap.ruleengine.component.IPropertyDisplay
import hap.ruleengine.parts.property.DoubleProperty
import hap.ruleengine.parts.property.IntProperty
import hap.ruleengine.parts.property.StringProperty
import javafx.scene.layout.StackPane
import tornadofx.*

/**
 * Created by Per Malmberg on 2017-01-20.
 */
class ViewBuilder(root: StackPane) : IPropertyDisplay {
    override fun show(property: DoubleProperty) {
        property.init()
        with(myForm) {
            field(property.header) {
                textfield {
                    bind(property.valueProperty())
//                    validator {
//                        try {
//                            it?.toDouble()
//                            null
//                        } catch (e: NumberFormatException) {
//                            error("Not a valid value")
//                        }
//                    }
                }
            }
        }
    }

    var myForm: Fieldset by singleAssign()

    override fun show(property: IntProperty) {
        property.init()
        with(myForm) {
            field(property.header) {
                textfield {
                    bind(property.valueProperty())
                }
            }
        }
    }

    override fun show(property: StringProperty) {
        property.init()
        with(myForm) {
            field(property.header) {
                textfield {
                    bind(property.valueProperty())
                }
            }
        }
    }

    init {
        with(root) {
            form {
                fieldset("Component Properties") { }.apply {
                    myForm = this
                }
            }
        }
    }
}