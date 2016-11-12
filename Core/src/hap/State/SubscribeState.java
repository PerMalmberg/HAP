package hap.state;

import chainedfsm.EnterChain;
import hap.event.FailureEvent;
import hap.event.MessageEvent;
import hap.event.SuccessEvent;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;

public class SubscribeState extends ModuleMonitorStateBase {

	public SubscribeState(ModuleMonitorFSM fsm) {
		super(fsm);

		new EnterChain<>(this, this::subscribe);
	}

	private void subscribe() {
		try {
			mySubscribeToken = myFsm.getClient().subscribe(getControlTopic("/#"), 2, null, myFsm, myFsm);
		} catch (MqttException e) {
			severe(e.getMessage());
			myFsm.setState(new ConnectState(myFsm));
		}
	}

	@Override
	public void accept(SuccessEvent e) {
		if (e.getToken() == mySubscribeToken) {
			myFsm.setState(new MonitorModuleState(myFsm));
		}
	}

	@Override
	public void accept(FailureEvent e) {
		if (e.getToken() == mySubscribeToken) {
			severe(e.toString());
			myFsm.setState(new ConnectState(myFsm));
		}
	}

	@Override
	public void accept(MessageEvent e) {

	}

	private IMqttToken mySubscribeToken;
}
