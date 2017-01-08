package hap.ruleengine.editor.viewmodel

import hap.ruleengine.editor.viewmodel.event.*
import hap.ruleengine.editor.viewmodel.parts.ComponentVM
import hap.ruleengine.editor.viewmodel.userinteraction.UserInteractionFSM
import hap.ruleengine.parts.CompositeSerializer
import hap.ruleengine.parts.composite.CompositeComponent
import javafx.application.Platform
import tornadofx.*
import java.io.File
import java.util.*

class DrawingSurfaceVM : ViewModel() {
    private var interaction : UserInteractionFSM by singleAssign()
    private var currentCC: CompositeComponent = CompositeComponent(UUID.randomUUID(), null)

    var surface: IDrawingSurfaceView by singleAssign()

    init {
        subscribeToEvents()
    }

    fun init( s: IDrawingSurfaceView )
    {
        surface = s
        interaction = UserInteractionFSM(surface)
    }

    private fun subscribeToEvents() {
        subscribe<DragComponentFromComponentPallet> {
            interaction.dragComponentFromComponentPallet(it.componentType)
        }

        subscribe<MouseDragDropReleased> {
            interaction.mouseDragDropReleased(it, surface, currentCC)
        }

        subscribe<OpenCompositeFromFile> {
            interaction.openComposite(this, it.window)
        }

        subscribe<SelectComponent> {
            interaction.selectComponent(it.component, it.addToOrRemoveFromSelection)
        }

        subscribe<ComponentDragged> {
            interaction.componentDragged(it)
        }

        subscribe<MouseReleased> {
            interaction.mouseReleased()
        }

        subscribe<SaveComposite> {
            interaction.saveComposite(this, it.window)
        }
    }

    private fun visualize() {
        // Visualize all components in the current composite
        currentCC.components
                .map {
                    val vm = ComponentVM(it)
                    vm.x.value = it.x
                    vm.y.value = it.y
                    surface.add(vm)
                }

        // Once all components are visualized, draw the wires.
        // It is run 'later' so that the GridPane has completed its layout phase.
        // If we don't wait for that to complete, we get invalid coordinates when calling boundsInParent etc.
        Platform.runLater {
            surface.drawWires(currentCC)
        }
    }

    fun setComposite(cc: CompositeComponent) {
        surface.clearComponents()
        currentCC = cc
        visualize()
    }

    fun saveComposite( file: File) : Boolean
    {
        val serializer = CompositeSerializer()
        return serializer.serialize(currentCC, file)
    }
}