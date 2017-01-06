package hap.ruleengine.editor.viewmodel.userinteraction.state

import chainedfsm.FSM
import hap.ruleengine.editor.viewmodel.DrawingSurfaceVM
import hap.ruleengine.editor.viewmodel.IDrawingSurfaceView
import hap.ruleengine.editor.viewmodel.userinteraction.state.NoAction
import hap.ruleengine.editor.viewmodel.userinteraction.state.UserInteractionState
import hap.ruleengine.parts.ComponentFactory
import javafx.stage.FileChooser
import javafx.stage.Window
import java.util.*
import chainedfsm.interfaces.IEnter
import chainedfsm.EnterChain




class OpenComposite constructor(fsm: FSM<UserInteractionState>, val view: DrawingSurfaceVM, val window: Window) : UserInteractionState(fsm) {

    private val factory = ComponentFactory()

    init {
        EnterChain(this, IEnter { this.enter() })
    }

    private fun enter() {
        val fc = FileChooser()
        fc.title = "Select composite to open"
        fc.extensionFilters.add(FileChooser.ExtensionFilter("Composite Files", "*.xml"))
        val file = fc.showOpenDialog(window)

        if (file != null) {
            val opened = factory.create(file, UUID.randomUUID())
            if (opened != null) {
                view.setComposite( opened )
            }
        }

        myFsm.setState(NoAction(myFsm))
    }

}