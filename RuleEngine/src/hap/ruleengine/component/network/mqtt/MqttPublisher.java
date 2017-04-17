package hap.ruleengine.component.network.mqtt;

import hap.ruleengine.component.IPropertyDisplay;
import hap.ruleengine.parts.composite.CompositeComponent;
import hap.ruleengine.parts.input.BooleanInput;
import hap.ruleengine.parts.input.Input;
import hap.ruleengine.parts.input.StringInput;
import hap.ruleengine.parts.output.BooleanOutput;
import hap.ruleengine.parts.property.StringProperty;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.UUID;

/**
 * Created by Per Malmberg on 2017-02-18.
 */
public class MqttPublisher extends MqttCommon
{
	private StringInput topicIn = new StringInput( "Topic", UUID.fromString( "2c8ac131-fc70-46f2-81d1-c7a94220b913" ), this );
	private StringInput dataIn = new StringInput( "Data", UUID.fromString( "d87ae8ee-c726-428d-89b4-87983b6cf26e" ), this );
	private BooleanInput clockIn = new BooleanInput( "Clk", UUID.fromString( "d377e7d8-9e22-4e08-ba30-589bc84cc754" ), this  );

	public MqttPublisher( UUID id, boolean executionAllowed )
	{
		super( id, executionAllowed, false );
	}

	public void setup( CompositeComponent cc )
	{
		addInput( dataIn );
		addInput( topicIn );
		addInput( clockIn );
		super.setup( cc );
	}

	@Override
	public void inputChanged( BooleanInput input )
	{
		// Only have a single boolean input.
		if( clockIn.getValue() )
		{
			String topic = ( getProperty( "Topic", "" ) + topicIn.getValue() ).trim();
			if( ! topic.isEmpty() )
			{
				connection.publish( topic, dataIn.getValue() );
			}
		}
	}

	@Override
	public void messageArrived( String topic, MqttMessage msg )
	{
		// Nothing
	}

	@Override
	public void showProperties( IPropertyDisplay display )
	{
		super.showProperties( display );
		display.show( new StringProperty( "Topic", "Topic", "", 2, "The topic to publish to. If the topic input has a value, then this setting becomes a prefix to that value.", this ) );
	}
}
