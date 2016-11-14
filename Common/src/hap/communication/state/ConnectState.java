// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.communication.state;

import chainedfsm.EnterChain;
import hap.communication.Communicator;
import hap.event.FailureEvent;
import hap.event.SuccessEvent;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.logging.Logger;

public class ConnectState extends CommState {

	public ConnectState(Communicator com) {
		super(com);
		myLog = com.getLogger();
		new EnterChain<>(this, this::enter);
	}

	private void enter() {
		try {

			if( myCom.getClient().isConnected() ) {
				myCom.getClient().disconnect();
			}

			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			connOpts.setAutomaticReconnect(true);
			connOpts.setConnectionTimeout(5);
			myCom.getClient().connect(connOpts, null, myCom);

		} catch (MqttException e) {
			myLog.severe("Exception during connect: " + e.getMessage());
			myLog.severe("Reason " + e.getReasonCode());
			myLog.severe("Cause " + e.getCause());
		}
	}

	@Override
	public void accept(SuccessEvent e) {
		myCom.setState( new SubscribeState(myCom));
	}

	@Override
	public void accept(FailureEvent e) {
		myLog.severe( e.toString() );
		enter(); // Retry
	}

	private Logger myLog;
}
