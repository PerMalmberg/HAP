package hap.ruleengine.component.bool

import hap.ruleengine.component.IPropertyDisplay
import hap.ruleengine.parts.Component
import hap.ruleengine.parts.composite.CompositeComponent
import hap.ruleengine.parts.output.BooleanOutput
import hap.ruleengine.parts.property.BooleanProperty
import java.util.*


class Fixed(id: UUID, executionState: Boolean) : Component(id, executionState)
{
	private var out = BooleanOutput("Out", UUID.fromString("6137525c-1c79-4655-9c66-bf6864cf8ba5"), this)

	override fun setup(cc: CompositeComponent)
	{
		addOutput(out)
		out.set(getProperty("Value", false))
	}

	override fun showProperties(display: IPropertyDisplay)
	{
		display.show(BooleanProperty("Value", "Value", false, "The fixed value", this))
	}

	override fun propertiesApplied()
	{
		out.set(getProperty("Value", false))
	}

}
