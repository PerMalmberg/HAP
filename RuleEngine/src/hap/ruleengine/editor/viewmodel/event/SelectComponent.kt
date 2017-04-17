package hap.ruleengine.editor.viewmodel.event

import hap.ruleengine.editor.viewmodel.parts.ComponentVM
import tornadofx.FXEvent

class SelectComponent(val component: ComponentVM, val addToOrRemoveFromSelection: Boolean) : FXEvent()