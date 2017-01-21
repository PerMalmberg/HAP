package hap.ruleengine.parts.property


import hap.ruleengine.parts.Component
import tornadofx.ViewModel
import tornadofx.getProperty
import tornadofx.onChange
import tornadofx.property

abstract class BaseProperty<T> internal constructor(val header: String, protected val key: String, protected val defaultValue: T, protected val comp: Component) : ViewModel(), IPropertyContainer {
    var value: T by property(defaultValue)
    fun valueProperty() = getProperty(BaseProperty<T>::value)

    abstract fun updateValue(v: T)
    abstract fun readValue() : T

    fun init()
    {
        value = readValue()
    }

    init {
        valueProperty().onChange {
            updateValue(value)
        }
    }
}