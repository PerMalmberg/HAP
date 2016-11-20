package hap.event;

import org.eclipse.paho.client.mqttv3.MqttMessage;


public class MessageEvent extends EventBase
{
private final String myTopic;
private final MqttMessage myMsg;

public MessageEvent( String topic, MqttMessage msg )
{
	myTopic = topic;
	myMsg = msg;
}

@Override
public void visit( IEventListener listener )
{
	listener.accept( this );
}

public String getTopic()
{
	return myTopic;
}

public MqttMessage getMsg()
{
	return myMsg;
}
}
