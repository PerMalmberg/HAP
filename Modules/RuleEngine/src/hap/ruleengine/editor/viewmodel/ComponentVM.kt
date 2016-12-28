package hap.ruleengine.editor.viewmodel

import hap.ruleengine.parts.IComponent
import hap.ruleengine.parts.input.BooleanInput
import hap.ruleengine.parts.input.DoubleInput
import hap.ruleengine.parts.input.IInput
import hap.ruleengine.parts.input.StringInput
import javafx.scene.paint.Color
import tornadofx.Controller
import java.util.*

///////////////////////////////////////////////////////////////////////////////
//
//
///////////////////////////////////////////////////////////////////////////////
class ComponentVM constructor(myComponent: IComponent) : Controller() {

    val name: String = myComponent.name
    val inputs: ArrayList<InputVM> = ArrayList()

    ///////////////////////////////////////////////////////////////////////////////
    //
    //
    ///////////////////////////////////////////////////////////////////////////////
    init {
        var index = 0
        myComponent.booleanInputs.values.map {
            inputs.add(InputVM(index++, getColor(it), it.name ))
        }
        myComponent.doubleInputs.values.map {
            inputs.add(InputVM(index++, getColor(it), it.name ))
        }
        myComponent.stringInputs.values.map {
            inputs.add(InputVM(index++, getColor(it), it.name ))
        }
    }

    ///////////////////////////////////////////////////////////////////////////////
    //
    //
    ///////////////////////////////////////////////////////////////////////////////
    private fun getColor(input: IInput): Color =
            when (input) {
                is BooleanInput -> Color.BLACK
                is DoubleInput -> Color.BEIGE
                is StringInput -> Color.CADETBLUE
                else -> Color.RED
            }
}