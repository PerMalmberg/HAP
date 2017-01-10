package hap.ruleengine.editor.viewmodel.event

import hap.ruleengine.editor.view.parts.ComponentView
import tornadofx.FXEvent

class DeleteComponent(val component: ComponentView) : FXEvent()