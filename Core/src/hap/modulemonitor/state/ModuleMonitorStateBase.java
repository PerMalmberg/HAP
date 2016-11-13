package hap.modulemonitor.state;

import chainedfsm.EnterLeaveState;
import hap.event.*;
import hap.message.Message;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.logging.Logger;

public abstract class ModuleMonitorStateBase extends EnterLeaveState implements IEventListener {
	public ModuleMonitorStateBase(ModuleMonitorFSM fsm) {
		myFsm = fsm;
	}

	protected void finest(String msg) {
		myLog.finest(msg);
	}

	public void tick() {
	}

	protected ModuleMonitorFSM myFsm;

	protected String getControlTopic(String subTopic) {
		return Message.combineTopic(Message.getTopicRoot(), "HAPControl", subTopic);
	}

	protected void publish(Message m) {
		MqttMessage mqtt = new MqttMessage(m.getPayload());
		mqtt.setQos(m.getQos().getValue());
		mqtt.setRetained(m.isRetained());

		try {
			myFsm.getClient().publish(m.getTopic(), mqtt, null, myFsm);
			finest("Publish: " + m.toString());
		} catch (MqttException e) {
			myLog.severe(e.getMessage());
		}
	}

	@Override
	public void accept(SuccessEvent e) {

	}

	@Override
	public void accept(FailureEvent e) {
		// Reconnect whenever something goes bad
		myLog.severe(e.toString());
		myFsm.setState(new ConnectState(myFsm));
	}

	@Override
	public void accept(MessageEvent e) {

	}

	@Override
	public void accept(PingTimeoutEvent e) {

	}

	private Logger myLog = Logger.getLogger("HAPCore");

}
