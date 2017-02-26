package hap.ruleengine.component.convert

import hap.ruleengine.parts.Component
import hap.ruleengine.parts.composite.CompositeComponent
import hap.ruleengine.parts.input.BooleanInput
import hap.ruleengine.parts.input.StringInput
import hap.ruleengine.parts.output.StringOutput
import java.util.*

/**
 * Created by Per Malmberg on 2017-02-25.
 */
class BoolToString(id: UUID, executionState: Boolean) : Component(id, executionState)
{
	private val inA = BooleanInput("A", UUID.fromString("1af1faef-b418-4349-abd3-f8fc6cf33827"), this)
	private val out = StringOutput("Out", UUID.fromString("cc52710e-d6e1-46f1-8682-e0d895355807"), this)

	override fun setup(cc: CompositeComponent?)
	{
		addInput(inA)
		addOutput(out)
	}

	override fun inputChanged(input: StringInput?)
	{
		out.set(if (inA.value == true) "true" else "false")
	}
}