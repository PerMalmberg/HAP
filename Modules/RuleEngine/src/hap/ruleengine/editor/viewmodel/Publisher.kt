package hap.ruleengine.editor.viewmodel

import tornadofx.ViewModel

// When we need access to the TornadoFX event system, but already inherit from a class we can't also inherit from
// ViewModel we use this utility publisher to call fire()
class Publisher : ViewModel()
