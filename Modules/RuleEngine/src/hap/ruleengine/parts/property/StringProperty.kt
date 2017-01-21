package hap.ruleengine.parts.property

import hap.ruleengine.parts.Component


class StringProperty(header: String, key: String, defaultValue: String, c: Component) : BaseProperty<String>(header, key, defaultValue, c) {
	override fun readValue(): String {
		return comp.getProperty(key, defaultValue)
	}

	override fun updateValue(v: String) {
		comp.setProperty(key, v)
	}
}
