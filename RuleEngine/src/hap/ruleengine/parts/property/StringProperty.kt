package hap.ruleengine.parts.property

import hap.ruleengine.parts.IComponentPropertyAccess
import tornadofx.ValidationMessage
import tornadofx.ValidationSeverity
import tornadofx.observable


class StringProperty(header: String, key: String, defaultValue: String, val minLength : Int, informationMessage: String, propReader: IComponentPropertyAccess) : BaseProperty<String>(header, key, defaultValue, informationMessage, propReader) {
    override fun validate(value: String?): ValidationMessage {
        var validationMessage = ValidationMessage(null, ValidationSeverity.Success)

        if (value == null || value.length < minLength ) {
            validationMessage = ValidationMessage("Must have a length of at least $minLength characters", ValidationSeverity.Error)
        }

        return validationMessage
    }

    val value = bind { observable(StringProperty::readValue, StringProperty::updateValue) } as javafx.beans.property.StringProperty

    override fun readValue(): String {
        return propReader.getProperty(key, defaultValue)
    }

    override fun updateValue(v: String) {
        propReader.setProperty(key, v)
    }
}
