package hap.message.cmd;

import hap.message.ControlTopic;
import hap.message.IMessageListener;
import hap.message.Message;

public class Ping extends Message
{

public Ping()
{
	this( ControlTopic.getControlTopic( "Ping" ), "*".getBytes(), QOS.AtMostOnce, false );
}

public Ping( String topic, byte[] payload, QOS qos, boolean retained )
{
	super( topic, payload, qos, retained );
}

@Override
public void visit( IMessageListener listener )
{
	listener.accept( this );
}
}
