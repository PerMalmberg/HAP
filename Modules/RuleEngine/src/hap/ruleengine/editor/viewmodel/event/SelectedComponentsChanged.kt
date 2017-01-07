package hap.ruleengine.editor.viewmodel.event

import hap.ruleengine.editor.viewmodel.parts.ComponentVM
import tornadofx.FXEvent
import java.util.*

class SelectedComponentsChanged(val selectedComponents: HashMap<UUID, ComponentVM>, val deselected: ArrayList<ComponentVM>) : FXEvent()