package hap.ruleengine.editor.viewmodel

import hap.ruleengine.editor.viewmodel.event.DragComponentFromComponentPallet
import hap.ruleengine.editor.viewmodel.event.MouseDragReleased
import hap.ruleengine.editor.viewmodel.event.OpenCompositeFromFile
import hap.ruleengine.editor.viewmodel.parts.ComponentVM
import hap.ruleengine.editor.viewmodel.userinteraction.UserInteractionFSM
import hap.ruleengine.parts.ComponentFactory
import hap.ruleengine.parts.composite.CompositeComponent
import javafx.application.Platform
import tornadofx.ViewModel
import tornadofx.singleAssign
import java.util.*

class DrawingSurfaceVM : ViewModel() {
    private val interaction = UserInteractionFSM()
    private var currentCC: CompositeComponent = CompositeComponent(UUID.randomUUID(), null)

    var view: IDrawingSurfaceView by singleAssign()

    init {
        subscribeToEvents()
    }

    private fun subscribeToEvents() {
        subscribe<DragComponentFromComponentPallet> {
            interaction.dragComponentFromComponentPallet(it.componentType)
        }

        subscribe<MouseDragReleased> {
            interaction.mouseDragReleased(it, view, currentCC)
        }

        subscribe<OpenCompositeFromFile> {
            interaction.openComposite(this, it.window)
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

    fun setComposite(cc: CompositeComponent) {
        view.clearComponents()
        currentCC = cc
        visualize()
    }

}