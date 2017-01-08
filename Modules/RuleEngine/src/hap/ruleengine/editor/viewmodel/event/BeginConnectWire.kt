package hap.ruleengine.editor.viewmodel.event

import hap.ruleengine.parts.IConnectionPoint
import javafx.geometry.Point2D
import tornadofx.FXEvent


class BeginConnectWire(val connectionPoint: IConnectionPoint, val sceneRelativeCenter: Point2D) : FXEvent()