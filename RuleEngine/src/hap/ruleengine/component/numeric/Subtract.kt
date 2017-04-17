// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.component.numeric

import hap.ruleengine.parts.Component
import hap.ruleengine.parts.composite.CompositeComponent
import hap.ruleengine.parts.input.DoubleInput
import hap.ruleengine.parts.output.DoubleOutput

import java.util.UUID


class Subtract(id: UUID, executionState: Boolean) : Component(id, executionState)
{
	private val inA = DoubleInput("A", UUID.fromString("62d2c8a5-8204-47d4-8263-eea5461a6225"), this)
	private val inB = DoubleInput("B", UUID.fromString("5c935a3a-42ca-412b-86b8-4c5a9e9a3e6b"), this)
	private val out = DoubleOutput("Out", UUID.fromString("83e071e4-0d9c-4da9-8ba4-1a4ccbe257d1"), this)

	override fun setup(cc: CompositeComponent)
	{
		addInput(inA)
		addInput(inB)
		addOutput(out)
	}

	override fun inputChanged(input: DoubleInput)
	{
		if (!java.lang.Double.isNaN(inA.value) && !java.lang.Double.isNaN(inB.value))
		{
			out.set(inA.value - inB.value)
		}
	}
}
