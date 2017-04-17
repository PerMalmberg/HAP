// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.component.numeric

import hap.ruleengine.parts.Component
import hap.ruleengine.parts.composite.CompositeComponent
import hap.ruleengine.parts.input.DoubleInput
import hap.ruleengine.parts.output.DoubleOutput

import java.util.UUID


class Divide(id: UUID, executionState: Boolean) : Component(id, executionState)
{
	private val inA = DoubleInput("A", UUID.fromString("0a64715f-ef3f-4e3b-8a19-5f380a0f8d4b"), this)
	private val inB = DoubleInput("B", UUID.fromString("bd2ff89c-7af0-49ec-ad00-dd58dc2dfd11"), this)
	private val out = DoubleOutput("Out", UUID.fromString("2fb9d78f-61ab-4c9f-841c-771e4b910dd0"), this)

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
			if( inB.value > 0 )
			{
				out.set(inA.value / inB.value)
			}
		}
	}
}
