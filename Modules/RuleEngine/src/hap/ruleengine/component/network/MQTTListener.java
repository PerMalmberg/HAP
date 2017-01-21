package hap.ruleengine.component.network;


import hap.ruleengine.component.IPropertyDisplay;
import hap.ruleengine.parts.Component;
import hap.ruleengine.parts.composite.CompositeComponent;
import hap.ruleengine.parts.property.DoubleProperty;
import hap.ruleengine.parts.property.IntProperty;
import hap.ruleengine.parts.property.StringProperty;

import java.util.UUID;

public class MQTTListener extends Component
{
	public MQTTListener( UUID id, boolean executionAllowed )
	{
		super( id, executionAllowed );
	}

	@Override
	public void setup( CompositeComponent cc )
	{

	}

	@Override
	public void showProperties( IPropertyDisplay display )
	{
		display.show( new StringProperty( "Broker", "Broker", "localhost", this ) );
		display.show( new IntProperty( "Port", "Port", 1883, this ) );
		display.show( new DoubleProperty( "D", "DK", 55.0, this ) );
	}
}
