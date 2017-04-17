// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.component.string

import hap.ruleengine.component.IPropertyDisplay
import hap.ruleengine.parts.Component
import hap.ruleengine.parts.composite.CompositeComponent
import hap.ruleengine.parts.input.StringInput
import hap.ruleengine.parts.output.StringOutput
import hap.ruleengine.parts.property.StringProperty
import java.util.*


class Format(id: UUID, executionState: Boolean) : Component(id, executionState)
{
	private var inputA = StringInput("A", UUID.fromString("4af0d4d9-3b60-4577-8812-82e81b5979a7"), this)
	private var inputB = StringInput("B", UUID.fromString("edfde957-f2bb-49d8-9710-67a2f9bc0748"), this)
	private var out = StringOutput("Out", UUID.fromString("a9711d6f-62e3-4da2-8c21-9b6928cfc3c6"), this)
	private val sb = StringBuilder()
	private val formatter = Formatter(sb)

	override fun setup(cc: CompositeComponent)
	{
		addInput(inputA)
		addInput(inputB)
		addOutput(out)
	}

	override fun showProperties(display: IPropertyDisplay)
	{
		display.show(StringProperty("Format", "Format", "%1\$s %2\$s", 2, "The java.util.Formatter format used to format the output value", this))
	}

	override fun inputChanged(input: StringInput)
	{
		Update()
	}

	private fun Update()
	{
		if (inputA.value != null && inputB.value != null)
		{
			try
			{
				sb.setLength(0)
				formatter.format(getProperty("Format", "%1\$s %2\$s"), inputA.value, inputB.value)
				out.set(sb.toString())
			}
			catch (ex: IllegalFormatException)
			{

			}
		}
	}

	override fun propertiesApplied()
	{
		Update()
	}
}
