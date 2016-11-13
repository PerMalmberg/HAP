package hap.message;

public abstract class RetainedMessage extends Message {

	public RetainedMessage(String topic, String payload, QOS qos ) {
		this(topic, payload.getBytes(), qos, true );
	}

	public RetainedMessage(String topic, byte[] payload, QOS qos, boolean retained ) {
		super(topic, payload, qos, retained);
	}
}
