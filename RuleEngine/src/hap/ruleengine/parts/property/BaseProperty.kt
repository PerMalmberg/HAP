package hap.ruleengine.parts.property


import hap.ruleengine.parts.IComponentPropertyAccess
import tornadofx.ValidationMessage
import tornadofx.ViewModel

abstract class BaseProperty<T : Any> internal constructor(val header: String, protected val key: String, protected val defaultValue: T, val informationMessage : String , protected val propReader: IComponentPropertyAccess) : ViewModel() {
    abstract fun updateValue(v: T)
    abstract fun readValue(): T
    abstract fun validate(value: String?): ValidationMessage
}