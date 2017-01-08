package hap.ruleengine.editor.viewmodel.event

import hap.ruleengine.parts.IConnectionPoint
import tornadofx.FXEvent


class BeginConnectWire(val connectionPoint: IConnectionPoint) : FXEvent()