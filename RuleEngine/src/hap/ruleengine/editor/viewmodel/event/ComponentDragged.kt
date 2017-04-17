package hap.ruleengine.editor.viewmodel.event

import hap.ruleengine.editor.viewmodel.parts.ComponentVM
import javafx.scene.input.MouseEvent
import tornadofx.FXEvent

class ComponentDragged(val event: MouseEvent, val vm: ComponentVM) : FXEvent()