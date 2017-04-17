package hap.ruleengine.component.convert

import hap.ruleengine.component.IPropertyDisplay
import hap.ruleengine.parts.Component
import hap.ruleengine.parts.composite.CompositeComponent
import hap.ruleengine.parts.input.StringInput
import hap.ruleengine.parts.output.DoubleOutput
import hap.ruleengine.parts.property.BooleanProperty
import hap.ruleengine.parts.property.DoubleProperty
import java.util.*

/**
 * Created by Per Malmberg on 2017-02-26.
 */
class StringToDouble(id: UUID, executionState: Boolean) : Component(id, executionState)
{
	private val inA = StringInput("A", UUID.fromString("12bfe6df-2f43-402f-9ff6-03bd0caf887c"), this)
	private val out = DoubleOutput("Out", UUID.fromString("89793b08-4fdb-4426-bbe7-0c023147207b"), this)

	override fun setup(cc: CompositeComponent?)
	{
		addInput(inA)
		addOutput(out)
	}

	override fun showProperties(display: IPropertyDisplay)
	{
		display.show(BooleanProperty("Enable error value", "EnableErrorValue", false, "If selected, the specified value will be output when the incoming value cannot be converted to a double.", this))
		display.show(DoubleProperty("Error value", "ErrorValue", 0.0, Double.MIN_VALUE, Double.MAX_VALUE, "The value to output if the input value is invalid", this))
	}

	override fun inputChanged(input: StringInput?)
	{
		try
		{
			val d = inA.value.toDouble()
			out.set(d)
		}
		catch(ex: NumberFormatException)
		{
			if( getProperty("OutputOnError", false))
			{
				out.set(getProperty("ErrorValue", 0.0))
			}
		}
	}
}