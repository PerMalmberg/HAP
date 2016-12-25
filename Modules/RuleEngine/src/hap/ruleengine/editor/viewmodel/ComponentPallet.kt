package hap.ruleengine.editor.viewmodel

import hap.ruleengine.parts.ComponentFactory
import hap.ruleengine.parts.IComponent
import hap.ruleengine.parts.composite.CompositeComponent
import hap.ruleengine.parts.data.ComponentDef
import tornadofx.Controller
import java.util.*


class ComponentPallet : Controller() {

    private val native: List<String>
    private val factory: ComponentFactory = ComponentFactory()

    val pallet: HashMap<String, NativeCategory> = HashMap()

    init {
        // Prepare native component names
        val comps = listOf(
                "bool.And",
                "numeric.Add",
                "string.Concatenate"
        ).map { "hap.ruleengine.component." + it }

        val nodes = listOf(
                "BooleanInputNode",
                "BooleanOutputNode",
                "DoubleInputNode",
                "DoubleOutputNode",
                "StringInputNode",
                "StringOutputNode"
        ).map { "hap.ruleengine.parts.node." + it }

        native = comps.plus(nodes)
    }

    ///////////////////////////////////////////////////////////////////////////////
    //
    //
    ///////////////////////////////////////////////////////////////////////////////
    fun loadComponents(): Boolean {
        var res = true

        val definition: ComponentDef = ComponentDef()
        definition.instanceId = UUID.randomUUID().toString()

        native.map {
            val category = it.substringBeforeLast('.');
            definition.name = it.replaceBeforeLast('.', "").replaceFirst(".", "")
            definition.nativeType = it
            val component = factory.create(definition, CompositeComponent())
            if (component == null) {
                res = false
            } else {
                CreateVMForComponent(category, component)
            }
        }

        return res
    }

    ///////////////////////////////////////////////////////////////////////////////
    //
    //
    ///////////////////////////////////////////////////////////////////////////////
    private fun CreateVMForComponent(category: String, component: IComponent) {
        val vm = ComponentVM(component)
        var cat = pallet[category]
        if (cat == null) {
            cat = NativeCategory(category)
            pallet.put(category, cat)
        }

        cat.components.add(vm)
    }

}