// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.component.bool

import hap.ruleengine.parts.Component
import hap.ruleengine.parts.composite.CompositeComponent
import hap.ruleengine.parts.input.BooleanInput
import hap.ruleengine.parts.output.BooleanOutput
import java.util.*

class Xor(id: UUID, executionState: Boolean) : Component(id, executionState)
{
	private var inA = BooleanInput("A", UUID.fromString("87cd2526-c95a-44a6-a7cd-8d94e97bd924"), this)
	private var inB = BooleanInput("B", UUID.fromString("d549c1a6-6624-4ada-a33f-b09918b43e57"), this)
	private var out = BooleanOutput("Out", UUID.fromString("6f48e6ab-a186-40c1-880e-2fbf2d5375c6"), this)

	override fun setup(cc: CompositeComponent)
	{
		addInput(inA)
		addInput(inB)
		addOutput(out)
	}

	override fun inputChanged(input: BooleanInput)
	{
		out.set(inA.value xor inB.value)
	}
}
