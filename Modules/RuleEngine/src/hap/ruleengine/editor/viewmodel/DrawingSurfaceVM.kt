package hap.ruleengine.editor.viewmodel

import hap.ruleengine.editor.view.parts.ComponentView
import hap.ruleengine.editor.viewmodel.event.EndComponentCreation
import hap.ruleengine.editor.viewmodel.event.StartComponentCreation
import hap.ruleengine.editor.viewmodel.parts.ComponentVM
import hap.ruleengine.parts.ComponentFactory
import hap.ruleengine.parts.composite.CompositeComponent
import tornadofx.ViewModel
import tornadofx.plusAssign
import java.util.*

class DrawingSurfaceVM : ViewModel() {
    private val currentCC: CompositeComponent = CompositeComponent(UUID.randomUUID(), null)
    private val factory: ComponentFactory = ComponentFactory()
    private var componentToCreate: String = ""

    init {
        subscribe<StartComponentCreation> {
            componentToCreate = it.componentType
        }

        subscribe<EndComponentCreation> {
            val c = factory.createFromName(componentToCreate, currentCC)
            componentToCreate = ""

            if (c != null) {
                // Component has been created, now visualize it.
                val localPosition = it.group.sceneToLocal(it.sceneX, it.sceneY)
                val cv = ComponentView(localPosition.x, localPosition.y, ComponentVM(c))
                it.group += cv
            }
        }
    }
}