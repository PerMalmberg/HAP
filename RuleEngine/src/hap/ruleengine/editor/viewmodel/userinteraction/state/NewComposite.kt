package hap.ruleengine.editor.viewmodel.userinteraction.state

import chainedfsm.EnterChain
import chainedfsm.interfaces.IEnter
import hap.ruleengine.editor.viewmodel.DrawingSurfaceVM
import hap.ruleengine.editor.viewmodel.userinteraction.UserInteractionFSM
import hap.ruleengine.parts.ComponentFactory
import hap.ruleengine.parts.composite.CompositeComponent
import javafx.stage.FileChooser
import javafx.stage.Window
import java.util.*


class NewComposite constructor(fsm: UserInteractionFSM, val surface: DrawingSurfaceVM, val window: Window) : UserInteractionState(fsm) {

    private val factory = ComponentFactory()

    init {
        EnterChain(this, IEnter { this.enter() })
    }

    private fun enter() {
        createComponentLibraryFolder()

        deselectAllComponents()
        surface.setComposite(CompositeComponent(UUID.randomUUID(), null, false))


        fsm.setState(NoAction(fsm))
    }

}