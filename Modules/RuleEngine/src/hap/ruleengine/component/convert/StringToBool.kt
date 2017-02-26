package hap.ruleengine.component.convert

import hap.ruleengine.parts.Component
import hap.ruleengine.parts.composite.CompositeComponent
import hap.ruleengine.parts.input.StringInput
import hap.ruleengine.parts.output.BooleanOutput
import java.util.*

/**
 * Created by Per Malmberg on 2017-02-25.
 */
class StringToBool(id: UUID, executionState: Boolean) : Component(id, executionState)
{
	private val inA = StringInput("A", UUID.fromString("8c71ecfa-640c-41f2-8eb7-13d02a5fba8f"), this)
	private val out = BooleanOutput("Out", UUID.fromString("9956d84f-102a-4fb1-810c-b7117b3c17c6"), this)

	override fun setup(cc: CompositeComponent?)
	{
		addInput(inA)
		addOutput(out)
	}

	override fun inputChanged(input: StringInput?)
	{
		out.set(inA.value.compareTo("true", true) == 0
				|| inA.value.compareTo("1", true) == 0
				|| inA.value.compareTo("on", true) == 0)
	}
}