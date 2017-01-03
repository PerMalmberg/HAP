package hap.ruleengine.editor.viewmodel

import hap.ruleengine.editor.view.parts.ComponentView
import hap.ruleengine.editor.viewmodel.event.EndComponentCreation
import hap.ruleengine.editor.viewmodel.event.OpenCompositeFromFile
import hap.ruleengine.editor.viewmodel.event.StartComponentCreation
import hap.ruleengine.editor.viewmodel.parts.ComponentVM
import hap.ruleengine.parts.ComponentFactory
import hap.ruleengine.parts.IComponent
import hap.ruleengine.parts.composite.CompositeComponent
import javafx.scene.Group
import javafx.stage.FileChooser
import tornadofx.ViewModel
import tornadofx.plusAssign
import tornadofx.singleAssign
import java.util.*

class DrawingSurfaceVM : ViewModel() {
    private var currentCC: CompositeComponent = CompositeComponent(UUID.randomUUID(), null)
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
                val localPosition = view.sceneToLocal(it.sceneX, it.sceneY)
                val cv = ComponentView(localPosition.x, localPosition.y, ComponentVM(c))
                view.add(cv)
            }
        }

        subscribe<OpenCompositeFromFile> {
            val fc = FileChooser()
            fc.title = "Select composite to open"
            fc.extensionFilters.add(FileChooser.ExtensionFilter("Composite Files", "*.xml"))
            val file = fc.showOpenDialog(it.window)
            if (file != null) {
                val opened = factory.create(file, UUID.randomUUID())
                if (opened != null) {
                    view.clearComponents()
                    currentCC = opened
                    visulize()
                }
            }
        }
    }

    private fun visulize() {
        // Visualize all components in the current composite
        currentCC.components
                .map { ComponentView(it.x, it.y, ComponentVM(it)) }
                .forEach { view.add(it) }
    }

    var view: IDrawingSurfaceView by singleAssign()
}