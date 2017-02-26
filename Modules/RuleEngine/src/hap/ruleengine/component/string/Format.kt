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
	private var inputA = StringInput("A", UUID.fromString("dff06bb5-94e3-4c6f-9357-287e5adc16d9"), this)
	private var inputB = StringInput("A", UUID.fromString("dff06bb5-94e3-4c6f-9357-287e5adc16d9"), this)
	private var out = StringOutput("Out", UUID.fromString("78dc1482-6159-40f5-9e40-747165e96423"), this)
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
}
