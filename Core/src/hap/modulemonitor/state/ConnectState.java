package hap.modulemonitor.state;


import chainedfsm.EnterChain;
import hap.event.FailureEvent;
import hap.event.SuccessEvent;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.logging.Logger;

public class ConnectState extends ModuleMonitorStateBase {

	public ConnectState(ModuleMonitorFSM fsm) {
		super(fsm);

		new EnterChain<>(this, this::enter);
	}


	private void enter() {
		try {

			if( myFsm.getClient().isConnected() ) {
				myFsm.getClient().disconnect();
			}

			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			connOpts.setAutomaticReconnect(true);
			connOpts.setConnectionTimeout(5);
			myFsm.getClient().connect(connOpts, null, myFsm);

		} catch (MqttException e) {
			myLog.severe("Exception during connect: " + e.getMessage());
			myLog.severe("Reason " + e.getReasonCode());
			myLog.severe("Cause " + e.getCause());
		}
	}

	@Override
	public void accept(SuccessEvent e) {
		myFsm.setState( new SubscribeState(myFsm));
	}

	@Override
	public void accept(FailureEvent e) {
		myLog.severe( e.toString() );
		enter(); // Retry
	}

	private static Logger myLog = Logger.getLogger("HAPCore");
}
