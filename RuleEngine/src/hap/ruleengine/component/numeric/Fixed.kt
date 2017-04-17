package hap.ruleengine.component.numeric

import hap.ruleengine.component.IPropertyDisplay
import hap.ruleengine.parts.Component
import hap.ruleengine.parts.composite.CompositeComponent
import hap.ruleengine.parts.output.DoubleOutput
import hap.ruleengine.parts.property.DoubleProperty
import hap.ruleengine.parts.property.StringProperty
import java.util.*


class Fixed(id: UUID, executionState: Boolean) : Component(id, executionState)
{
	private var out = DoubleOutput("Out", UUID.fromString("1f0e4058-e7ec-4bf2-9a78-573425006a72"), this)

	override fun setup(cc: CompositeComponent)
	{
		addOutput(out)
		out.set(getProperty("Value", 0.0))
	}

	override fun showProperties(display: IPropertyDisplay)
	{
		display.show(DoubleProperty("Value", "Value", 0.0, Double.MIN_VALUE, Double.MAX_VALUE, "The fixed value", this))
	}

	override fun propertiesApplied()
	{
		out.set(getProperty("Value", 0.0))
	}

}
