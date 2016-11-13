package hap.event;

import org.eclipse.paho.client.mqttv3.IMqttToken;

public class FailureEvent extends EventBase {
	public FailureEvent(IMqttToken token, Throwable throwable) {
		myToken = token;
		myThrowable = throwable;
	}

	@Override
	public void visit(IEventListener listener) {
		listener.accept(this);
	}

	private final IMqttToken myToken;
	private final Throwable myThrowable;

	public IMqttToken getToken() {
		return myToken;
	}

	public Throwable getThrowable() {
		return myThrowable;
	}

	@Override
	public String toString()
	{
		String s = "";
		s += getThrowable() == null ? "" : getThrowable().getCause();
		return s;
	}
}
