package hap.ruleengine.editor.viewmodel.userinteraction.state

import chainedfsm.EnterChain
import chainedfsm.interfaces.IEnter
import hap.ruleengine.editor.viewmodel.DrawingSurfaceVM
import hap.ruleengine.editor.viewmodel.userinteraction.UserInteractionFSM
import hap.ruleengine.parts.ComponentFactory
import javafx.stage.FileChooser
import javafx.stage.Window


class SaveComposite(fsm: UserInteractionFSM, val surface: DrawingSurfaceVM, val window: Window) : UserInteractionState(fsm) {
    init {
        EnterChain(this, IEnter { enter() })
    }

    fun enter() {
        createComponentLibraryFolder()

        var file = fsm.currentFile
        if (file != null) {
            surface.saveComposite(file)
        } else {
            val fc = FileChooser()
            fc.title = "Save Composite"
            fc.extensionFilters.add(FileChooser.ExtensionFilter("Composite Files (*.cc)", "*.cc"))
            fc.initialDirectory = ComponentFactory.STANDARD_LIBRARY.toFile()
            file = fc.showSaveDialog(window)
            if (file != null) {
                surface.saveComposite(file)
                fsm.currentFile = file
            }
        }
        fsm.setState(NoAction(fsm))
    }
}