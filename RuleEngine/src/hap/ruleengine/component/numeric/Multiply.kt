// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.component.numeric

import hap.ruleengine.parts.Component
import hap.ruleengine.parts.composite.CompositeComponent
import hap.ruleengine.parts.input.DoubleInput
import hap.ruleengine.parts.output.DoubleOutput

import java.util.UUID


class Multiply(id: UUID, executionState: Boolean) : Component(id, executionState)
{
	private val inA = DoubleInput("A", UUID.fromString("bf8a57db-ceaf-4ae0-9a7d-0c2fade1807b"), this)
	private val inB = DoubleInput("B", UUID.fromString("394a9e5e-4fa4-45d2-80bb-7809d2fd1b30"), this)
	private val out = DoubleOutput("Out", UUID.fromString("0ca5d59a-ad42-4a5c-8996-6c88961b117d"), this)

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
			out.set(inA.value * inB.value)
		}
	}
}
