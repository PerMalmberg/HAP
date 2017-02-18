package hap.ruleengine.component.network;

import hap.ruleengine.parts.composite.CompositeComponent;
import hap.ruleengine.parts.input.StringInput;
import hap.ruleengine.parts.output.StringOutput;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Per Malmberg on 2017-02-18.
 */
public class MqttSubscriber extends MqttCommon
{
	private StringOutput dataOut;
	private final ConcurrentLinkedQueue<Pair> incoming = new ConcurrentLinkedQueue<>();

	public MqttSubscriber( UUID id, boolean executionAllowed )
	{
		super( id, executionAllowed );
	}

	public void setup( CompositeComponent cc )
	{
		dataOut = new StringOutput( "Data", UUID.fromString( "3268b8b9-c544-4b0a-ac7c-fdeb64031d48" ), this );
		addOutput( dataOut );
		super.setup( cc );
	}


	@Override
	public void tick()
	{
		super.tick();

		if( getExecutionState() )
		{
			if( ! incoming.isEmpty() )
			{
				Pair msg = incoming.poll();
				dataOut.set( new String( msg.msg.getPayload() ) );
			}
		}
	}

	@Override
	public void messageArrived( String topic, MqttMessage msg )
	{
		incoming.add( new Pair( topic, msg ) );
	}
}
