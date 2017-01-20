package hap.ruleengine.editor.viewmodel

import hap.SysUtil
import hap.ruleengine.RuleEngine
import hap.ruleengine.editor.viewmodel.parts.ComponentVM
import hap.ruleengine.editor.viewmodel.parts.CompositeVM
import hap.ruleengine.parts.ComponentFactory
import hap.ruleengine.parts.IComponent
import hap.ruleengine.parts.composite.CompositeComponent
import hap.ruleengine.parts.data.ComponentDef
import tornadofx.Controller
import java.io.File
import java.util.*
import java.util.zip.ZipFile


class ComponentPallet : Controller() {

    private val nativeComponents: ArrayList<String> = ArrayList()
    private val factory: ComponentFactory = ComponentFactory()

    val categories: ArrayList<NativeCategory> = ArrayList()

    init {
        // Prepare native component names
        val jar = SysUtil.getFullOrRelativePath(RuleEngine::class.java, "HAPed.jar")

        val f = File(jar)
        if (f.exists()) {
            // Running from a jar
            ZipFile(jar).use {
                it.entries().toList().filter {
                    o ->
                    !o.isDirectory
                            && (!o.name.contains("placeholder") && o.name.contains("hap/ruleengine/component/") || o.name.contains("hap/ruleengine/parts/node/"))
                }.map {
                    nativeComponents.add(it.name.replace('/', '.').removeSuffix(".class"))
                }
            }
        } else {
            // Running in IntelliJ
            val start = SysUtil.getFullOrRelativePath(RuleEngine::class.java, "RuleEngine")
            val dir = File(start)
            dir.walkTopDown().toList().filter {
                val asUnix = it.absolutePath.replace('\\', '/')
                !it.isDirectory &&
                        (!asUnix.contains("placeholder") && asUnix.contains("hap/ruleengine/component/") || asUnix.contains("hap/ruleengine/parts/node/"))
            }.map {
                val asUnix = it.absolutePath.removePrefix(start).replace('\\', '/').removePrefix("/").removeSuffix(".class")
                nativeComponents.add(asUnix.replace('/', '.'))
            }
        }

        loadComponents()
        loadComposites()
    }

    ///////////////////////////////////////////////////////////////////////////////
    //
    //
    ///////////////////////////////////////////////////////////////////////////////
    private fun loadComponents(): Boolean {
        var res = true

        categories.clear()

        val definition: ComponentDef = ComponentDef()
        definition.instanceId = UUID.randomUUID().toString()

        nativeComponents.map {
            val category = it.substringBeforeLast('.')
            definition.name = it.replaceBeforeLast('.', "").replaceFirst(".", "")
            definition.nativeType = it
            val component = factory.create(definition, CompositeComponent(false))
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
        val vm = ComponentVM(component, false)
        val cat = getCategory(category)
        cat.components.add(vm)
    }

    ///////////////////////////////////////////////////////////////////////////////
    //
    //
    ///////////////////////////////////////////////////////////////////////////////
    private fun CreateVMForComposite(sourceFile: String, category: String, component: CompositeComponent) {
        val vm = CompositeVM( sourceFile, component, false)
        val cat = getCategory(category)
        cat.components.add(vm)
    }

    private fun getCategory(category: String): NativeCategory {
        var cat = categories.find {
            it.category == category
        }

        if (cat == null) {
            cat = NativeCategory(category)
            categories.add(cat)
        }

        return cat
    }

    private fun loadComposites() {
        val fact = ComponentFactory()
        fact.availableImports.forEach {
            val cc = fact.create(File(it), UUID.randomUUID())
            if (cc != null) {
                CreateVMForComposite(it, "Library", cc)
            }
        }
    }

}