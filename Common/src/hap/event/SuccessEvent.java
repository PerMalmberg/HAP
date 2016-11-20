package hap.event;


import org.eclipse.paho.client.mqttv3.IMqttToken;

public class SuccessEvent extends EventBase
{
private final IMqttToken myToken;

public SuccessEvent( IMqttToken token )
{
	myToken = token;
}

@Override
public void visit( IEventListener listener )
{
	listener.accept( this );
}

public IMqttToken getToken()
{
	return myToken;
}
}
