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

        // https://regex101.com/r/yJu9l6/1
        val findCompExpr = "(((hap\\/ruleengine\\/component\\/)|(hap\\/ruleengine\\/parts\\/node\\/)).*)\\.class$"
        val pattern = Regex(findCompExpr)

        // Prepare native component names

        if (SysUtil.isRunningFromJAR(javaClass)) {
            // Running from a jar
            ZipFile(SysUtil.getNameOfJarForClass(javaClass)).use {
                it.entries().toList().filter { o -> !o.isDirectory && !o.name.contains("placeholder") }.forEach {
                    ReadComponentFromFile(it.toString(), pattern)
                }
            }
        } else {
            // Running in IntelliJ
            val start = SysUtil.getFullOrRelativePath(RuleEngine::class.java, "")
            val dir = File(start)
            dir.walkTopDown().toList().filter { !it.isDirectory && !it.name.contains("placeholder") }.forEach {
                ReadComponentFromFile(it.absolutePath, pattern)
            }
        }

        loadComponents()
        loadComposites()
    }


    private fun ReadComponentFromFile(fullPath: String, pattern: Regex) {
        val asUnix = fullPath.replace('\\', '/')
        val singleMatch = pattern.find(asUnix)
        if (singleMatch != null) {
            // We always want group 1
            val compName = singleMatch.groups[1]?.value
            if (compName != null) {
                nativeComponents.add(compName.replace('/', '.'))
            }
        }
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
        val vm = CompositeVM(sourceFile, component, false)
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