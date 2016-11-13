package hap.event;

import org.eclipse.paho.client.mqttv3.MqttMessage;


public class MessageEvent extends EventBase {
	@Override
	public void visit(IEventListener listener) {
		listener.accept(this);
	}

	public MessageEvent( String topic, MqttMessage msg )
	{
		myTopic = topic;
		myMsg = msg;
	}

	private final String myTopic;
	private final MqttMessage myMsg;

	public String getTopic() {
		return myTopic;
	}

	public MqttMessage getMsg() {
		return myMsg;
	}
}
