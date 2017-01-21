package hap.ruleengine.parts.property


import hap.ruleengine.parts.Component

class BooleanProperty(header: String, key: String, defaultValue: Boolean, c: Component) : BaseProperty<Boolean>(header, key, defaultValue, c) {
    override fun readValue(): Boolean {
        return comp.getProperty(key, defaultValue)
    }

    override fun updateValue(v: Boolean) {
        comp.setProperty(key, v)
    }
}
