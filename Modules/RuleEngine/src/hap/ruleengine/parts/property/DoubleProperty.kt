package hap.ruleengine.parts.property

import hap.ruleengine.parts.IComponentPropertyAccess
import tornadofx.ValidationMessage
import tornadofx.ValidationSeverity
import tornadofx.observable


class DoubleProperty(header: String, key: String, defaultValue: Double,informationMessage: String, propReader: IComponentPropertyAccess) : BaseProperty<Double>(header, key, defaultValue, informationMessage, propReader) {
    override fun validate(value: String?): ValidationMessage {
        var validationMessage = ValidationMessage(null, ValidationSeverity.Success)

        var res = true

        if (value != null) {
            try {
                java.lang.Double.parseDouble(value)
                res = value.all { it.isDigit() || it == '.' }
            } catch (e: NumberFormatException) {
                res = false
            }
        }

        if (!res) {
            validationMessage = ValidationMessage("Invalid value", ValidationSeverity.Error)
        }

        return validationMessage
    }

    val value = bind { observable(DoubleProperty::readValue, DoubleProperty::updateValue) } as javafx.beans.property.DoubleProperty

    override fun readValue(): Double {
        return propReader.getProperty(key, defaultValue)
    }

    override fun updateValue(v: Double) {
        propReader.setProperty(key, v)
    }
}