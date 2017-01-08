package view

import hap.ruleengine.editor.view.RuleEngineTab
import javafx.event.Event
import javafx.scene.control.TabPane
import tornadofx.View
import tornadofx.tab


class MainView : View("HAPed") {
    override val root = TabPane()

    init {
        with(root) {
            // TODO: Let user open/close tabs based on menu selection in main widow
            tab("Rule editor") {
                setOnCloseRequest(Event::consume) // Don't let the user close the tab
                add(RuleEngineTab::class)
            }
            tab("Other module configuration")
            {

            }
        }
    }
}
