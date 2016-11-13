package hap.message;

public abstract class NonRetainedMessage extends Message {

	public NonRetainedMessage(String topic, String payload, QOS qos) {
		this(topic, payload.getBytes(), qos, false);
	}

	public NonRetainedMessage(String topic, byte[] payload, QOS qos, boolean retained) {
		super(topic, payload, qos, retained);
	}
}
