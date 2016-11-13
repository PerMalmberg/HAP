package hap.modulemonitor.state;

import chainedfsm.EnterChain;
import hap.event.SuccessEvent;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.logging.Logger;

public class SubscribeState extends ModuleMonitorStateBase {

	public SubscribeState(ModuleMonitorFSM fsm) {
		super(fsm);

		new EnterChain<>(this, this::subscribe);
	}

	private void subscribe() {
		try {
			mySubscribeToken = myFsm.getClient().subscribe(getControlTopic("/#"), 2, null, myFsm, myFsm);
		} catch (MqttException e) {
			myLog.severe(e.getMessage());
			myFsm.setState(new ConnectState(myFsm));
		}
	}

	@Override
	public void accept(SuccessEvent e) {
		if (e.getToken() == mySubscribeToken) {
			myFsm.setState(new MonitorModuleState(myFsm));
		}
	}

	private IMqttToken mySubscribeToken;

	private Logger myLog = Logger.getLogger("HAPCore");
}
