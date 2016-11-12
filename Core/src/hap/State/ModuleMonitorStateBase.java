package hap.state;

import chainedfsm.EnterLeaveState;
import hap.event.*;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public abstract class ModuleMonitorStateBase extends EnterLeaveState implements IEventListener {
	public ModuleMonitorStateBase(ModuleMonitorFSM fsm) {
		myFsm = fsm;
	}

	protected String combineTopic(final String topicRoot, final String... topic) {
		String t = topicRoot;

		for (String curr : topic) {
			if (!curr.startsWith("/")) {
				curr = "/" + curr;
			}

			if (curr.endsWith("/")) {
				curr = curr.substring(0, curr.length() - 1);
			}

			t += curr;
		}

		return t;
	}

	protected void severe(String msg) {
		myFsm.getLogger().severe(msg);
	}

	protected void info(String msg) {
		myFsm.getLogger().info(msg);
	}

	public void tick() {
	}

	protected ModuleMonitorFSM myFsm;

	protected String getControlTopic(String subTopic) {
		return combineTopic(myFsm.getTopicRoot(), "HAPControl", subTopic);
	}

	protected IMqttToken command(String command, String payload) {
		IMqttToken token = null;
		try {
			token = myFsm.getClient().publish(combineTopic(myFsm.getTopicRoot(), "/Command/", command), new MqttMessage(payload.getBytes()), null, myFsm);
			info("Sent Ping-command");
		} catch (MqttException e) {
			severe(e.getMessage());
		}

		return token;
	}

	@Override
	public void accept(SuccessEvent e) {

	}

	@Override
	public void accept(FailureEvent e) {

	}

	@Override
	public void accept(MessageEvent e) {

	}

	@Override
	public void accept(PingTimeoutEvent e) {

	}
}
