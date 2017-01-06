package hap.ruleengine.editor.viewmodel

import hap.ruleengine.editor.viewmodel.event.EndComponentCreation
import hap.ruleengine.editor.viewmodel.event.OpenCompositeFromFile
import hap.ruleengine.editor.viewmodel.event.StartComponentCreation
import hap.ruleengine.editor.viewmodel.parts.ComponentVM
import hap.ruleengine.parts.ComponentFactory
import hap.ruleengine.parts.composite.CompositeComponent
import javafx.application.Platform
import javafx.stage.FileChooser
import tornadofx.ViewModel
import tornadofx.singleAssign
import java.io.File
import java.util.*

class DrawingSurfaceVM : ViewModel() {
    private var currentCC: CompositeComponent = CompositeComponent(UUID.randomUUID(), null)
    private val factory: ComponentFactory = ComponentFactory()
    private var componentToCreate: String = ""

    var view: IDrawingSurfaceView by singleAssign()

    init {
        subscribe<StartComponentCreation> {
            componentToCreate = it.componentType
        }

        subscribe<EndComponentCreation> {
            val c = factory.createFromName(componentToCreate, currentCC)
            componentToCreate = ""

            if (c != null) {
                // Component has been created, now visualize it.
                // Component has been created, now visualize it.
                val localPosition = view.sceneToLocal(it.sceneX, it.sceneY)
                val vm = ComponentVM(c)
                vm.x.value = localPosition.x
                vm.y.value = localPosition.y
                view.add(vm)
            }
        }

        subscribe<OpenCompositeFromFile> {
            val fc = FileChooser()
            fc.title = "Select composite to open"
            fc.extensionFilters.add(FileChooser.ExtensionFilter("Composite Files", "*.xml"))
            //val file = fc.showOpenDialog(it.window)
            // QQQ
            val file = File("d:\\git\\HAP\\Modules\\RuleEngine\\testdata\\LoadCompositeWithImportTest.xml")
            if (file != null) {
                val opened = factory.create(file, UUID.randomUUID())
                if (opened != null) {
                    view.clearComponents()
                    currentCC = opened
                    visualize()
                }
            }
        }
    }

    private fun visualize() {
        // Visualize all components in the current composite
        currentCC.components
                .map {
                    val vm = ComponentVM(it)
                    vm.x.value = it.x
                    vm.y.value = it.y
                    view.add(vm)
                }

        // Once all components are visualized, draw the wires.
        Platform.runLater {
            view.drawWires(currentCC)
        }
    }

}