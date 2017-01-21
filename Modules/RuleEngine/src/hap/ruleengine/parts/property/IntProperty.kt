package hap.ruleengine.parts.property

import hap.ruleengine.parts.Component


class IntProperty(header: String, key: String, defaultValue: Int, c: Component) : BaseProperty<Int>(header, key, defaultValue, c) {
	override fun readValue(): Int {
		return comp.getProperty(key, defaultValue)
	}

	override fun updateValue(v: Int) {
		comp.setProperty(key, v)
	}
}
