package hap.ruleengine.editor.viewmodel.event

import hap.ruleengine.editor.view.parts.WireView
import tornadofx.FXEvent


class DeleteWire(val wire: WireView) : FXEvent()