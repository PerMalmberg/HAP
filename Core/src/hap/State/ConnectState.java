package hap.state;


import chainedfsm.EnterChain;
import hap.event.FailureEvent;
import hap.event.MessageEvent;
import hap.event.SuccessEvent;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class ConnectState extends ModuleMonitorStateBase {

	public ConnectState(ModuleMonitorFSM fsm) {
		super(fsm);

		new EnterChain<>(this, this::enter);
	}


	private IMqttToken myToken;

	private void enter() {
		try {

			if( myFsm.getClient().isConnected() ) {
				myFsm.getClient().disconnect();
			}

			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			connOpts.setAutomaticReconnect(true);
			connOpts.setConnectionTimeout(5);
			myToken = myFsm.getClient().connect(connOpts, null, myFsm);

		} catch (MqttException e) {
			myFsm.getLogger().severe("Exception during connect: " + e.getMessage());
			myFsm.getLogger().severe("Reason " + e.getReasonCode());
			myFsm.getLogger().severe("Cause " + e.getCause());
		}
	}

	@Override
	public void accept(SuccessEvent e) {
		myFsm.setState( new SubscribeState(myFsm));
	}

	@Override
	public void accept(FailureEvent e) {
		severe( e.toString() );
		enter();
	}

	@Override
	public void accept(MessageEvent e) {

	}
}
