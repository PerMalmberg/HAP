package hap.ruleengine.editor.viewmodel.parts

import hap.ruleengine.editor.viewmodel.parts.InputVM
import hap.ruleengine.editor.viewmodel.parts.OutputVM
import hap.ruleengine.parts.Component
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
import tornadofx.*
import java.util.*

///////////////////////////////////////////////////////////////////////////////
//
//
///////////////////////////////////////////////////////////////////////////////
class ComponentVM constructor( val component: IComponent) : ViewModel() {

    val name: String = component.name
    val inputs: ArrayList<InputVM> = ArrayList()
    val outputs: ArrayList<OutputVM> = ArrayList()

    ///////////////////////////////////////////////////////////////////////////////
    //
    //
    ///////////////////////////////////////////////////////////////////////////////
    init {
        // Mark the component as visualized so that inputs/outputs that should not be
        // shown are handled correctly. Specifically, this applies to nodes.
        component.setVisualized()

        var index = 0
        component.booleanInputs.values.map {
            inputs.add(InputVM(index++, getColor(it), it))
        }
        component.doubleInputs.values.map {
            inputs.add(InputVM(index++, getColor(it), it))
        }
        component.stringInputs.values.map {
            inputs.add(InputVM(index++, getColor(it), it))
        }

        index = 0
        component.booleanOutputs.values.map {
            outputs.add(OutputVM(index++, getColor(it), it))
        }
        component.doubleOutputs.values.map {
            outputs.add(OutputVM(index++, getColor(it), it))
        }
        component.stringOutputs.values.map {
            outputs.add(OutputVM(index++, getColor(it), it))
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

    val componentType: String = component.javaClass.name
    val x = bind { component.observable(IComponent::getX, IComponent::setX) }
    val y = bind { component.observable(IComponent::getY, IComponent::setY) }
}