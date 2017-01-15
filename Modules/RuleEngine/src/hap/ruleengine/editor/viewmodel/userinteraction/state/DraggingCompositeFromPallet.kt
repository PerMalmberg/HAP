package hap.ruleengine.editor.viewmodel.userinteraction.state

import hap.ruleengine.editor.viewmodel.IDrawingSurfaceView
import hap.ruleengine.editor.viewmodel.event.MouseDragDropReleased
import hap.ruleengine.editor.viewmodel.parts.ComponentVM
import hap.ruleengine.editor.viewmodel.parts.CompositeVM
import hap.ruleengine.editor.viewmodel.userinteraction.UserInteractionFSM
import hap.ruleengine.parts.ComponentFactory
import hap.ruleengine.parts.composite.CompositeComponent
import java.io.File
import java.util.*

class DraggingCompositeFromPallet constructor(val sourceFile: String, fsm: UserInteractionFSM) : UserInteractionState(fsm) {
    private val factory = ComponentFactory()

    override fun mouseDragDropReleased(event: MouseDragDropReleased, view: IDrawingSurfaceView, currentCC: CompositeComponent) {
        val c = factory.create(File(sourceFile), UUID.randomUUID(), currentCC )
        if (c != null) {
            // Component has been created, now visualize it.
            val localPosition = view.sceneToLocal(event.sceneX, event.sceneY)
            val vm = CompositeVM(sourceFile, c)
            vm.x.value = localPosition.x
            vm.y.value = localPosition.y
            view.add(vm)
        }

        fsm.setState(NoAction(fsm))
    }

    override fun endDragComponentFromComponentPallet() {
        fsm.setState(NoAction(fsm))
    }
}