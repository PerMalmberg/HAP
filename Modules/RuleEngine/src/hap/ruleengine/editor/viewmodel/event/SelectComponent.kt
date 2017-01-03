package hap.ruleengine.editor.viewmodel.event

import hap.ruleengine.editor.viewmodel.parts.ComponentVM
import tornadofx.FXEvent

class SelectComponent constructor( val component : ComponentVM) : FXEvent()