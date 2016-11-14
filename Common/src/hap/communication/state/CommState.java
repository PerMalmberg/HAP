// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.communication.state;

import hap.communication.Communicator;
import hap.event.*;
import hap.message.IMessageListener;
import hap.message.Message;
import hap.message.cmd.Ping;
import hap.message.general.UnclassifiedMessage;
import hap.message.response.PingResponse;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.logging.Logger;

public class CommState extends chainedfsm.EnterLeaveState implements IEventListener, IMessageListener {

	public CommState(Communicator com) {
		myCom = com;
		myLog = com.getLogger();
	}

	public void tick() {

	}

	protected Communicator myCom;

	protected void publish(Message m) {
		myCom.publish(m.getTopic(), m.getPayload(), m.getQos(), m.isRetained());
		myLog.finest("Publish: " + m.toString());
	}

	@Override
	public void accept(SuccessEvent e) {

	}

	@Override
	public void accept(FailureEvent e) {
		myLog.warning("Failure: " + e.toString());
		myLog.warning("Reconnecting");
		myCom.setState(new ConnectState(myCom));
	}

	@Override
	public void accept(MessageEvent e) {

	}

	@Override
	public void accept(PingTimeoutEvent e) {

	}

	@Override
	public void accept(ConnectionLostEvent e) {
		myLog.warning("Lost connection: " + e.getThrowable().getMessage() );
	}

	@Override
	public void accept(Ping msg) {

	}

	@Override
	public void accept(PingResponse msg) {

	}

	@Override
	public void accept(UnclassifiedMessage msg) {

	}

	private final Logger myLog;
}
