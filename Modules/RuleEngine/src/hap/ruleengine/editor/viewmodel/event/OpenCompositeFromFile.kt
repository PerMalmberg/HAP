package hap.ruleengine.editor.viewmodel.event

import javafx.stage.Window
import tornadofx.FXEvent

class OpenCompositeFromFile constructor( val window:Window, val liveComponents:Boolean) : FXEvent()