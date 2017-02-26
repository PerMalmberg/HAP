package hap.ruleengine.component.convert

import hap.ruleengine.component.IPropertyDisplay
import hap.ruleengine.parts.Component
import hap.ruleengine.parts.composite.CompositeComponent
import hap.ruleengine.parts.input.DoubleInput
import hap.ruleengine.parts.input.StringInput
import hap.ruleengine.parts.output.BooleanOutput
import hap.ruleengine.parts.property.DoubleProperty
import java.util.*

/**
 * Created by Per Malmberg on 2017-02-25.
 */
class DoubleToBool(id: UUID, executionState: Boolean) : Component(id, executionState)
{
	private val inA = DoubleInput("A", UUID.fromString("baa7e43f-c5de-4619-81f9-2c14721dacf2"), this)
	private val out = BooleanOutput("Out", UUID.fromString("024f0b69-f642-49e4-8bbf-4237ab387a53"), this)

	override fun setup(cc: CompositeComponent?)
	{
		addInput(inA)
		addOutput(out)
	}

	override fun showProperties(display: IPropertyDisplay)
	{
		display.show(DoubleProperty("Zero-Hysteresis", "Hysteresis", 0.01, 0.0, Double.MAX_VALUE, "The maximum deviation from 0.0 to consider the incoming value 0.0. I.e value of 0.01 means values > -0.01 and < 0.01 are considered to be 0.", this))
	}

	override fun inputChanged(input: StringInput?)
	{
		out.set(Math.abs(inA.value) < getProperty("Hysteresis", 0.01))
	}
}