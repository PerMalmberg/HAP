package hap.message.response;


import hap.message.ControlTopic;
import hap.message.IMessageListener;
import hap.message.NonRetainedMessage;

public class PingResponse extends NonRetainedMessage {

	public PingResponse( String moduleName ) {
		super(ControlTopic.getControlTopic("PingResponse"), moduleName, QOS.AtMostOnce);
	}

	public PingResponse(String topic, byte[] payload, QOS qos, boolean retained)
	{
		super(topic, payload, qos, retained);
	}

	public String getModuleName()
	{
		return new String( getPayload() );
	}

	@Override
	public void visit(IMessageListener listener) {
		listener.accept(this);
	}
}
