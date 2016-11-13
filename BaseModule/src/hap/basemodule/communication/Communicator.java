// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.basemodule.communication;

import hap.message.ControlTopic;
import hap.message.IMessageListener;
import hap.message.Message;
import hap.message.MessageFactory;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Logger;

public class Communicator implements IMqttActionListener, IMqttMessageListener {

	public Communicator(String broker, String clientId, IMessageListener listener, Logger logger) {
		myBroker = broker;
		myClientId = clientId;
		myListener = listener;
		myLog = logger;
	}

	public void start() {
		try {
			myClient = new MqttAsyncClient(myBroker, myClientId, new MemoryPersistence());
			connect();
		} catch (MqttException e) {
			myLog.severe(e.getMessage());
			myLog.severe("Connection attempt failed, aborting.");
		}
	}

	private void connect() throws MqttException {
		myIsConnectInProgress = true;
		MqttConnectOptions options = new MqttConnectOptions();
		options.setAutomaticReconnect(true);
		options.setConnectionTimeout(5);
		options.setCleanSession(true);
		myToken = myClient.connect(options, null, this);
	}

	@Override
	public void onSuccess(IMqttToken iMqttToken) {
		if (iMqttToken == myToken) {
			// Connect success
			myIsConnectInProgress = false;
			myLog.finest("Connected to broker " + myBroker);

			try {
				String t = ControlTopic.getControlTopic("Ping");
				myClient.subscribe(t, 2, null, this, this);
			} catch (MqttException e) {
				myLog.severe(e.getMessage());
			}
		}
	}

	@Override
	public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
		myIsConnectInProgress = false;
		myLog.severe("Failed to connect to broker, retrying: " + throwable.getMessage());
		reconnect();
	}

	private void reconnect() {
		try {
			connect();
		} catch (MqttException e) {
			myLog.severe("Reconnection attempt failed, aborting.");
		}
	}

	public void tick() {
		if (myClient.isConnected()) {
			while (!myMessage.isEmpty()) {
				Message m = myMessage.pop();
				if (m != null) {
					m.visit(myListener);
				}
			}
		} else if (!myIsConnectInProgress) {
			reconnect();
		}
	}

	@Override
	public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
		Message m = myMessageFactory.Create(topic, mqttMessage);
		if (m != null) {
			myMessage.push(m);
		}
	}

	public void subscribe(String topic, Message.QOS qos) {
		try {
			myClient.subscribe(topic, qos.getValue(), null, this, this);
		} catch (MqttException ex) {
			myLog.severe(ex.getMessage());
		}
	}

	public void publish(String topic, byte[] payload, Message.QOS qos, boolean retained) {
		try {
			myClient.publish(topic, payload, qos.getValue(), retained, null, this);
		} catch (MqttException e) {
			myLog.severe(e.getMessage());
		}
	}

	private IMqttToken myToken = null;
	private boolean myIsConnectInProgress = false;
	private final MessageFactory myMessageFactory = new MessageFactory();
	private final ConcurrentLinkedDeque<Message> myMessage = new ConcurrentLinkedDeque<>();
	private final IMessageListener myListener;
	private IMqttAsyncClient myClient = null;
	private final String myBroker;
	private final String myClientId;
	private final Logger myLog;

}
