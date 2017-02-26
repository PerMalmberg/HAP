// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.component.string

import hap.ruleengine.parts.Component
import hap.ruleengine.parts.input.StringInput
import hap.ruleengine.parts.output.StringOutput
import hap.ruleengine.parts.composite.CompositeComponent

import java.util.UUID


class Concatenate(id: UUID, executionState: Boolean) : Component(id, executionState)
{
	private var inA = StringInput("A", UUID.fromString("4bf0d092-7195-4fd2-9540-c1978164fe50"), this)
	private var inB = StringInput("B", UUID.fromString("36abe6cb-97f1-462e-88e5-4538cea29041"), this)
	private var out = StringOutput("Out", UUID.fromString("90d5fe91-8938-46da-b155-711bc10511fb"), this)

	override fun setup(cc: CompositeComponent)
	{
		addInput(inA)
		addInput(inB)
		addOutput(out)
	}

	override fun inputChanged(input: StringInput)
	{
		if (inA.value != null && inB.value != null)
		{
			out.set(inA.value + inB.value)
		}
	}
}
