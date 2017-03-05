package hap.ruleengine.component.network;


import hap.ruleengine.component.IPropertyDisplay;
import hap.ruleengine.component.network.mqtt.ConnectionInfo;
import hap.ruleengine.component.network.mqtt.MqttConnection;
import hap.ruleengine.parts.Component;
import hap.ruleengine.parts.composite.CompositeComponent;
import hap.ruleengine.parts.output.BooleanOutput;
import hap.ruleengine.parts.property.BooleanProperty;
import hap.ruleengine.parts.property.IntProperty;
import hap.ruleengine.parts.property.StringProperty;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.UUID;

public abstract class MqttCommon extends Component implements IMqttMessageReceiver
{
	MqttCommon( UUID id, boolean executionAllowed, boolean doSubscription )
	{
		super( id, executionAllowed );
		this.doSubscription = doSubscription;
	}

	protected MqttConnection connection = new MqttConnection(this);

	private boolean doSubscription;

	private boolean connectionStatus = false;
	private BooleanOutput isConnected;


	class Pair
	{
		Pair( String t, MqttMessage m )
		{
			topic = t;
			msg = m;
		}

		final String topic;
		final MqttMessage msg;
	}


	public void setup( CompositeComponent cc )
	{
		isConnected = new BooleanOutput( "Connected", UUID.fromString( "46ac2e40-ccc0-4499-826b-1009bed9211b" ), this );
		addOutput( isConnected );
	}


	@Override
	public void tearDown()
	{
		connection.disconnect();
	}

	@Override
	public void propertiesApplied()
	{
		if( getExecutionState() )
		{
			connection.reconnect();
		}
	}

	private void connect()
	{
		connection.connect();
	}

	@Override
	protected void executionStateChanged( boolean executionState )
	{
		if( executionState )
		{
			connect();
		}
		else
		{
			connection.disconnect();
		}
	}


	@Override
	public void tick()
	{
		if( isConnected.getValue() != connectionStatus )
		{
			isConnected.set( connectionStatus );
		}
	}

	@Override
	public void showProperties( IPropertyDisplay display )
	{
		display.show( new StringProperty( "Broker", "Broker", "localhost", 1, "The broker to which a connection is made", this ) );
		display.show( new IntProperty( "Port", "Port", 0, 0, 65535, "The port of the broker to which a connection is made. Leave at 0 for default port.", this ) );
		display.show( new StringProperty( "User", "User", "", 0, "The user name used when connecting to the broker", this ) );
		display.show( new StringProperty( "Password", "Password", "", 0, "The password used when connecting to the broker", this ) );
		display.show( new StringProperty( "Topic", "Topic", "", 2, "The topic to subscribe to", this ) );
		display.show( new BooleanProperty( "Secure connection", "Secure", false, "Use secure communication", this ) );
	}

	@Override
	public ConnectionInfo getConnectionInfo()
	{
		return new ConnectionInfo(
				getProperty( "Broker", "localhost" ),
				getProperty( "User", "" ),
				getProperty( "Password", "" ),
				getProperty( "Port", 0 ),
				getProperty( "Secure", false ),
				doSubscription ? getProperty( "Topic", "" ) : "" );
	}

	@Override
	public void connectionStatus( boolean isConnected )
	{
		connectionStatus = isConnected;
	}

}
