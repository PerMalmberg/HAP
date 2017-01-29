package hap.ruleengine.parts.property

import hap.ruleengine.parts.IComponentPropertyAccess
import javafx.beans.property.IntegerProperty
import tornadofx.ValidationMessage
import tornadofx.ValidationSeverity
import tornadofx.observable


class IntProperty(header: String, key: String, defaultValue: Int, val minValue: Int, val maxValue: Int, informationMessage: String, propReader: IComponentPropertyAccess) : BaseProperty<Int>(header, key, defaultValue, informationMessage, propReader)
{
	override fun validate(value: String?): ValidationMessage
	{
		var validationMessage = ValidationMessage(null, ValidationSeverity.Success)

		var res = true

		if (value != null)
		{
			try
			{
				val v = java.lang.Integer.parseInt(value, 10)
				res = value.all { it.isDigit() || it == '-' }
				res = res && v >= minValue && v <= maxValue
			}
			catch (e: NumberFormatException)
			{
				res = false
			}
		}

		if (!res)
		{
			validationMessage = ValidationMessage("Invalid value", ValidationSeverity.Error)
		}

		return validationMessage
	}

	val value = bind { observable(IntProperty::readValue, IntProperty::updateValue) } as IntegerProperty

	override fun readValue(): Int
	{
		return propReader.getProperty(key, defaultValue)
	}

	override fun updateValue(v: Int)
	{
		propReader.setProperty(key, v)
	}
}
