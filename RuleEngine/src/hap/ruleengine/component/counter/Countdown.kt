// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.component.counter

import hap.ruleengine.component.IPropertyDisplay
import hap.ruleengine.parts.Component
import hap.ruleengine.parts.composite.CompositeComponent
import hap.ruleengine.parts.input.BooleanInput
import hap.ruleengine.parts.output.BooleanOutput
import hap.ruleengine.parts.property.IntProperty
import java.time.Duration
import java.time.Instant
import java.util.*


class Countdown(id: UUID, executionState: Boolean) : Component(id, executionState)
{
	private var reset = BooleanInput("Reset", UUID.fromString("6e8fe669-4a0c-4ac5-a7d6-397b3615acf4"), this)
	private var trigger = BooleanInput("Trigger", UUID.fromString("7752513a-a3df-4ee6-a923-d90fc112d19c"), this)
	private var enabled = BooleanInput("Enabled", UUID.fromString("8516eb6f-9355-4d52-9459-6549c6bc8b9b"), this)
	private var expired = BooleanOutput("Expired", UUID.fromString("ee4351e9-dd24-4c60-b47e-9f2590c32603"), this)
	private var timeLeft = Duration.ZERO
	private var lastTick = Instant.now()
	private var countingDown = false

	override fun setup(cc: CompositeComponent)
	{
		addInput(reset)
		addInput(trigger)
		addInput(enabled)
		addOutput(expired)
		Reset()
	}

	override fun showProperties(display: IPropertyDisplay)
	{
		display.show(IntProperty("Timeout (s)", "Timeout", 0, 0, Int.MAX_VALUE, "Timeout, in seconds", this))
	}

	override fun tick()
	{
		if (enabled.value && countingDown)
		{
			if (timeLeft > Duration.ZERO)
			{
				val now = Instant.now()
				val elapsed = Duration.between(lastTick, now)
				lastTick = now
				timeLeft -= elapsed
				if (timeLeft <= Duration.ZERO)
				{
					expired.set(true)
				}
			}
		}
	}

	override fun inputChanged(input: BooleanInput)
	{
		if (input == reset && reset.value)
		{
			Reset()
		}
		else if (input == trigger && trigger.value && enabled.value)
		{
			countingDown = true
			lastTick = Instant.now()
		}
		else if (input == enabled && !enabled.value)
		{
			countingDown = false
		}
	}

	override fun propertiesApplied()
	{
		Reset()
	}

	private fun Reset()
	{
		val t = getProperty("Timeout", 0).toLong()
		timeLeft = Duration.ofSeconds(t)
		expired.set(false)
		countingDown = false
	}
}
