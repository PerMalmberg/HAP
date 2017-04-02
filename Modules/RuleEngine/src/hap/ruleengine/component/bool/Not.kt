// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.component.bool

import hap.ruleengine.parts.input.BooleanInput
import hap.ruleengine.parts.output.BooleanOutput
import hap.ruleengine.parts.Component
import hap.ruleengine.parts.composite.CompositeComponent

import java.util.UUID

class Not(id: UUID, executionState: Boolean) : Component(id, executionState)
{
	private val inA = BooleanInput("A", UUID.fromString("abd9a91c-4dee-4d07-bf8b-326f59fcacfd"), this)
	private val out = BooleanOutput("Out", UUID.fromString("35a0222f-feb7-433f-88eb-7cd45f3e0964"), this)

	override fun setup(cc: CompositeComponent)
	{
		addInput(inA)
		addOutput(out)
	}

	override fun inputChanged(input: BooleanInput)
	{
		out.set(!inA.value)
	}
}
