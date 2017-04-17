package hap.ruleengine.component.string

import hap.ruleengine.component.IPropertyDisplay
import hap.ruleengine.parts.Component
import hap.ruleengine.parts.composite.CompositeComponent
import hap.ruleengine.parts.output.StringOutput
import hap.ruleengine.parts.property.StringProperty
import java.util.*


class Fixed(id: UUID, executionState: Boolean) : Component(id, executionState)
{
	private var out = StringOutput("Out", UUID.fromString("90d5fe91-8938-46da-b155-711bc10511fb"), this)

	override fun setup(cc: CompositeComponent)
	{
		addOutput(out)
		out.set(getProperty("Value", ""))
	}

	override fun showProperties(display: IPropertyDisplay)
	{
		display.show(StringProperty("Value", "Value", "", 0, "The fixed value", this))
	}

	override fun propertiesApplied()
	{
		out.set(getProperty("Value", ""))
	}

}
