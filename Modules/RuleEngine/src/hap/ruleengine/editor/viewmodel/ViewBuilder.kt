package hap.ruleengine.editor.viewmodel

import hap.ruleengine.component.IPropertyDisplay
import hap.ruleengine.parts.property.IntProperty
import hap.ruleengine.parts.property.StringProperty
import javafx.scene.layout.StackPane
import tornadofx.*

/**
 * Created by Per Malmberg on 2017-01-20.
 */
class ViewBuilder(root: StackPane) : IPropertyDisplay {
    var myForm: Fieldset by singleAssign()

    override fun show(property: IntProperty) {
        with(myForm) {
            field(property.header) {
                textfield {
                    bind(property.observable(IntProperty::getValue, IntProperty::setValue))
                }
            }
        }
    }

    override fun show(property: StringProperty?) {

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