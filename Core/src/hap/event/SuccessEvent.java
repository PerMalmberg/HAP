package hap.event;


import org.eclipse.paho.client.mqttv3.IMqttToken;

public class SuccessEvent extends EventBase {
	@Override
	public void visit(IEventListener listener) {
		listener.accept(this);
	}

	public SuccessEvent( IMqttToken token )
	{
		myToken = token;
	}

	private final IMqttToken myToken;

	public IMqttToken getToken() {
		return myToken;
	}
}
