package hap.message.response;


import hap.message.ControlTopic;
import hap.message.IMessageListener;
import hap.message.Message;

public class StopResponse extends Message
{

public StopResponse( String moduleName, boolean success )
{
	this( ControlTopic.getControlTopic( StopResponse.class.getSimpleName() ), ( moduleName + ":" + success ).getBytes(), QOS.AtMostOnce, false );
}

public StopResponse( String topic, byte[] payload, QOS qos, boolean retained )
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
