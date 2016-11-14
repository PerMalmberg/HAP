package hap.communication.state;

import chainedfsm.EnterChain;
import hap.communication.Communicator;
import hap.event.SuccessEvent;
import hap.message.ControlTopic;
import hap.message.Message;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import java.util.logging.Logger;

public class SubscribeState extends CommState {

	public SubscribeState(Communicator com) {
		super(com);
		myLog = com.getLogger();
		new EnterChain<>(this, this::subscribe);
	}

	private void subscribe() {
		myCom.subscribe(ControlTopic.getControlTopic("/#"), Message.QOS.AtMostOnce);
	}

	@Override
	public void accept(SuccessEvent e) {
		// Pass control to the applications entry state
		myCom.setState(myCom.getStateProvider().createEntryState(myCom));
	}


	private Logger myLog;
}
