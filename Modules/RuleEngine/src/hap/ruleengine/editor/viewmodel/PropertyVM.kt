package hap.ruleengine.editor.viewmodel

import hap.ruleengine.editor.viewmodel.event.SelectComponent
import tornadofx.ViewModel


class PropertyVM : ViewModel()
{
    init {
        subscribe<SelectComponent> {

        }
    }

}