package hap.ruleengine.component.network.mqtt;

import hap.ruleengine.component.IPropertyDisplay;
import hap.ruleengine.parts.composite.CompositeComponent;
import hap.ruleengine.parts.output.BooleanOutput;
import hap.ruleengine.parts.output.StringOutput;
import hap.ruleengine.parts.property.StringProperty;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Per Malmberg on 2017-02-18.
 */
public class MqttSubscriber extends MqttCommon
{
	private StringOutput dataOut;
	private StringOutput topicOut;
	private BooleanOutput clockOut;
	private final ConcurrentLinkedQueue<Pair> incoming = new ConcurrentLinkedQueue<>();

	public MqttSubscriber( UUID id, boolean executionAllowed )
	{
		super( id, executionAllowed, true );
	}

	public void setup( CompositeComponent cc )
	{
		dataOut = new StringOutput( "Data", UUID.fromString( "3268b8b9-c544-4b0a-ac7c-fdeb64031d48" ), this );
		topicOut = new StringOutput( "Topic", UUID.fromString( "7750cf89-5fca-4ac3-b663-e6a9edbea263" ), this );
		clockOut = new BooleanOutput( "Clk", UUID.fromString( "2f6f14b3-d208-47fe-86a5-950f55fdb280" ), this );
		addOutput( topicOut );
		addOutput( dataOut );
		addOutput( clockOut );
		super.setup( cc );
	}


	@Override
	public void tick()
	{
		super.tick();

		if( ! incoming.isEmpty() )
		{
			Pair msg = incoming.poll();
			topicOut.set( msg.topic  );
			dataOut.set( new String( msg.msg.getPayload() ) );
			clockOut.toggle();
		}
	}

	@Override
	public void messageArrived( String topic, MqttMessage msg )
	{
		incoming.add( new Pair( topic, msg ) );
	}

	@Override
	public void showProperties( IPropertyDisplay display )
	{
		super.showProperties( display );
		display.show( new StringProperty( "Topic", "Topic", "", 2, "The topic to subscribe to.", this ) );
	}
}
