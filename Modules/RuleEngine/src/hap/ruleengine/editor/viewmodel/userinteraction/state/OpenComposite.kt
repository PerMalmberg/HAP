package hap.ruleengine.editor.viewmodel.userinteraction.state

import chainedfsm.EnterChain
import chainedfsm.interfaces.IEnter
import hap.ruleengine.editor.viewmodel.DrawingSurfaceVM
import hap.ruleengine.editor.viewmodel.userinteraction.UserInteractionFSM
import hap.ruleengine.parts.ComponentFactory
import javafx.stage.FileChooser
import javafx.stage.Window
import java.util.*


class OpenComposite constructor(fsm: UserInteractionFSM, val surface: DrawingSurfaceVM, val window: Window) : UserInteractionState(fsm) {

    private val factory = ComponentFactory()

    init {
        EnterChain(this, IEnter { this.enter() })
    }

    private fun enter() {
        createComponentLibraryFolder()

        val fc = FileChooser()
        fc.title = "Select composite to open"
        fc.extensionFilters.add(FileChooser.ExtensionFilter("Composite Files (*.cc)", "*.cc"))
        fc.initialDirectory = ComponentFactory.STANDARD_LIBRARY.toFile()
        val file = fc.showOpenDialog(window)

        if (file != null) {
            val opened = factory.create(file, UUID.randomUUID())
            if (opened != null) {
                surface.setComposite(opened)
            }
        }

        fsm.setState(NoAction(fsm))
    }

}