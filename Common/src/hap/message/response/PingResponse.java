package hap.message.response;


import hap.message.ControlTopic;
import hap.message.IMessageListener;
import hap.message.Message;

public class PingResponse extends Message
{

public PingResponse( String moduleName )
{
	this( ControlTopic.getControlTopic( "PingResponse" ), moduleName.getBytes(), QOS.AtMostOnce, false );
}

public PingResponse( String topic, byte[] payload, QOS qos, boolean retained )
{
	super( topic, payload, qos, retained );
}

public String getModuleName()
{
	return new String( getPayload() );
}

@Override
public void visit( IMessageListener listener )
{
	listener.accept( this );
}
}
