package hap.ruleengine.editor.viewmodel.parts

import hap.ruleengine.editor.viewmodel.parts.InputVM
import hap.ruleengine.editor.viewmodel.parts.OutputVM
import hap.ruleengine.parts.IComponent
import hap.ruleengine.parts.IConnectionPoint
import hap.ruleengine.parts.input.BooleanInput
import hap.ruleengine.parts.input.DoubleInput
import hap.ruleengine.parts.input.IInput
import hap.ruleengine.parts.input.StringInput
import hap.ruleengine.parts.output.BooleanOutput
import hap.ruleengine.parts.output.DoubleOutput
import hap.ruleengine.parts.output.StringOutput
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
    val outputs: ArrayList<OutputVM> = ArrayList()

    ///////////////////////////////////////////////////////////////////////////////
    //
    //
    ///////////////////////////////////////////////////////////////////////////////
    init {
        var index = 0
        myComponent.booleanInputs.values.filter { it.isVisibleOnComponent }.map {
            inputs.add(InputVM(index++, getColor(it), it ))
        }
        myComponent.doubleInputs.values.filter { it.isVisibleOnComponent }.map {
            inputs.add(InputVM(index++, getColor(it), it ))
        }
        myComponent.stringInputs.values.filter { it.isVisibleOnComponent }.map {
            inputs.add(InputVM(index++, getColor(it), it ))
        }

        index = 0
        myComponent.booleanOutputs.values.filter { it.isVisibleOnComponent }.map {
            outputs.add(OutputVM(index++, getColor(it), it ))
        }
        myComponent.doubleOutputs.values.filter { it.isVisibleOnComponent }.map {
            outputs.add(OutputVM(index++, getColor(it), it ))
        }
        myComponent.stringOutputs.values.filter { it.isVisibleOnComponent }.map {
            outputs.add(OutputVM(index++, getColor(it), it ))
        }
    }

    ///////////////////////////////////////////////////////////////////////////////
    //
    //
    ///////////////////////////////////////////////////////////////////////////////
    private fun getColor(input: IConnectionPoint): Color =
            when (input) {
                is BooleanInput -> Color.BLACK
                is BooleanOutput -> Color.BLACK
                is DoubleInput -> Color.CYAN
                is DoubleOutput -> Color.CYAN
                is StringInput -> Color.DEEPPINK
                is StringOutput -> Color.DEEPPINK
                else -> Color.RED
            }

    val  componentType: String = myComponent.javaClass.name
}