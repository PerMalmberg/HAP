package hap.ruleengine.editor.viewmodel.event

import hap.ruleengine.parts.IConnectionPoint
import tornadofx.FXEvent


class MouseEnteredConnectionPoint(val connectionPoint: IConnectionPoint?) : FXEvent()