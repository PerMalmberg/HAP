package hap.ruleengine.component.network;


import hap.ruleengine.component.IPropertyDisplay;
import hap.ruleengine.parts.Component;
import hap.ruleengine.parts.composite.CompositeComponent;
import hap.ruleengine.parts.output.BooleanOutput;
import hap.ruleengine.parts.output.StringOutput;
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
		dataOut = new StringOutput( "Data", UUID.fromString( "3268b8b9-c544-4b0a-ac7c-fdeb64031d48" ), this );
		isConnected = new BooleanOutput( "Connected", UUID.fromString( "46ac2e40-ccc0-4499-826b-1009bed9211b" ), this );

		addOutput( dataOut );
		addOutput( isConnected );

	}

	@Override
	public void showProperties( IPropertyDisplay display )
	{
		display.show( new StringProperty( "Broker", "Broker", "localhost", 1, "The broker to which a connection is made", this ) );
		display.show( new IntProperty( "Port", "Port", 1883, 1, 65535, "The port of the broker to which a connection is made", this ) );
		display.show( new StringProperty( "Topic", "topic", "", 2, "The topic to subscribe to", this ) );
	}

	private StringOutput dataOut;
	private BooleanOutput isConnected;
}
