package hap.ruleengine.component.placeholder;

import hap.ruleengine.parts.Component;
import hap.ruleengine.parts.composite.CompositeComponent;

import java.util.UUID;

public class PlaceHolderComponent extends Component
{
	public PlaceHolderComponent()
	{
		super( UUID.randomUUID(), false );
	}

	@Override
	public void setup( CompositeComponent cc )
	{

	}
}
