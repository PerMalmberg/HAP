package hap.ruleengine.editor.viewmodel.event

import javafx.scene.Group
import tornadofx.FXEvent

class EndComponentCreation(val group: Group, val sceneX: Double, val sceneY: Double) : FXEvent()