package hap.message.cmd;

import hap.message.IMessageListener;
import hap.message.NonRetainedMessage;

public class Ping extends NonRetainedMessage {

	public Ping()
	{
		super( "Ping", "*", QOS.AtMostOnce );
	}

	public Ping(String topic, byte[] payload, QOS qos, boolean retained)
	{
		super(topic, payload, qos, retained);
	}

	@Override
	public void visit(IMessageListener listener) {
		listener.accept(this);
	}
}
