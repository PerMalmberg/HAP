package hap.ruleengine.component.network;

import hap.ruleengine.parts.composite.CompositeComponent;
import hap.ruleengine.parts.input.StringInput;
import hap.ruleengine.parts.output.BooleanOutput;
import hap.ruleengine.parts.output.StringOutput;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.UUID;

/**
 * Created by Per Malmberg on 2017-02-18.
 */
public class MqttPublisher extends MqttCommon
{
	private StringInput dataIn;

	public MqttPublisher( UUID id, boolean executionAllowed )
	{
		super( id, executionAllowed );
	}

	public void setup( CompositeComponent cc )
	{
		dataIn = new StringInput( "Data", UUID.fromString( "d87ae8ee-c726-428d-89b4-87983b6cf26e" ), this );
		addInput( dataIn );
		super.setup( cc );
	}

	@Override
	public void inputChanged( StringInput input )
	{
		// Only have a single input..
		String topic = getProperty( "topic", "" );
		proxy.publish( topic, input.getValue() );
	}

	@Override
	public void messageArrived( String topic, MqttMessage msg )
	{

	}
}
