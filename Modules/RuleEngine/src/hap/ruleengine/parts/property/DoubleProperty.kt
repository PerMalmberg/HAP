package hap.ruleengine.parts.property

import hap.ruleengine.parts.Component


class DoubleProperty(header: String, key: String, defaultValue: Double, c: Component) : BaseProperty<Double>(header, key, defaultValue, c) {
	override fun readValue(): Double {
		return comp.getProperty(key, defaultValue)
	}

	override fun updateValue(v: Double) {
		comp.setProperty(key, v)
	}
}