package hap.ruleengine.component.convert

import hap.ruleengine.component.IPropertyDisplay
import hap.ruleengine.parts.Component
import hap.ruleengine.parts.composite.CompositeComponent
import hap.ruleengine.parts.input.DoubleInput
import hap.ruleengine.parts.input.StringInput
import hap.ruleengine.parts.output.StringOutput
import hap.ruleengine.parts.property.IntProperty
import java.util.*

/**
 * Created by Per Malmberg on 2017-02-26.
 */
class DoubleToString(id: UUID, executionState: Boolean) : Component(id, executionState)
{
	private val inA = DoubleInput("A", UUID.fromString("66e6267e-4e6e-485d-8481-0d217ea991ff"), this)
	private val out = StringOutput("Out", UUID.fromString("ac6ea32a-7aed-4686-8efa-7fafafad704a"), this)

	override fun setup(cc: CompositeComponent?)
	{
		addInput(inA)
		addOutput(out)
	}

	override fun showProperties(display: IPropertyDisplay)
	{
		display.show(IntProperty("Decimals", "Decimals", 2, 0, Int.MAX_VALUE, "Decimal count", this))
	}

	fun Double.format(digits: Int): String
	{
		return java.lang.String.format("%.${digits}f", this)
	}

	override fun inputChanged(input: StringInput?)
	{
		out.set(inA.value.format(getProperty("Decimals", 2)))
	}
}