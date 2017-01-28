package hap.ruleengine.parts.property


import hap.ruleengine.parts.IComponentPropertyAccess
import tornadofx.ValidationMessage
import tornadofx.ValidationSeverity
import tornadofx.observable

class BooleanProperty(header: String, key: String, defaultValue: Boolean, informationMessage: String, propReader: IComponentPropertyAccess) : BaseProperty<Boolean>(header, key, defaultValue, informationMessage, propReader) {
    val value = bind { observable(BooleanProperty::readValue, BooleanProperty::updateValue) } as javafx.beans.property.BooleanProperty

    override fun validate(value: String?): ValidationMessage {
        return ValidationMessage(null, ValidationSeverity.Success)
    }

    override fun readValue(): Boolean {
        return propReader.getProperty(key, defaultValue)
    }

    override fun updateValue(v: Boolean) {
        propReader.setProperty(key, v)
    }
}
