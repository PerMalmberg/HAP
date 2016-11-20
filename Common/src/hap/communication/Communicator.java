// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.communication;

import hap.communication.state.CommState;
import hap.communication.state.ConnectState;
import hap.event.ConnectionLostEvent;
import hap.event.EventBase;
import hap.event.FailureEvent;
import hap.event.SuccessEvent;
import hap.event.timed.TimedComparator;
import hap.event.timed.TimedEvent;
import hap.event.timed.TimedEventBase;
import hap.message.Message;
import hap.message.MessageFactory;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

public class Communicator extends chainedfsm.FSM<CommState> implements IPublisher, IMqttMessageListener, IMqttActionListener, MqttCallback {

	public Communicator(String broker, String clientId, Logger logger) {
		myBroker = broker;
		myClientId = clientId;
		myLog = logger;
	}

	public boolean start(IModuleRunner stateProvider) {
		boolean res = true;
		myStateProvider = stateProvider;

		try {
			myClient = new MqttAsyncClient(myBroker, myClientId, new MemoryPersistence());
			myClient.setCallback(this);
			setState(new ConnectState(this));
		} catch (MqttException e) {
			res = false;
			myLog.severe(e.getMessage());
			myLog.severe("Connection attempt failed, aborting.");
		}

		return res;
	}

	public void tick() {
		if (getCurrentState() != null) {
			while (!myEvent.isEmpty()) {
				EventBase e = myEvent.poll();
				e.visit(getCurrentState());
			}

			TimedEvent te = timedQueue.peek();
			while (te != null && te.getInstant().isBefore(Instant.now())) {
				te = timedQueue.poll();
				te.getEvent().execute();
				// Get next possible event
				te = timedQueue.peek();
			}

			while (!myMessage.isEmpty()) {
				Message m = myMessage.pop();
				if (m != null) {
					m.visit(getCurrentState());
				}
			}
		}

		getCurrentState().tick();

		if (myClient.isConnected() && myDoResubscribe) {
			myDoResubscribe = false;

			for (String topic : mySubscriptions.keySet()) {
				subscribe(topic, mySubscriptions.get(topic));
			}

		}
	}

	@Override
	public void preSetState()
	{
		// Reset time events every time we change state
		timedQueue.clear();
	}

	@Override
	public void connectionLost(Throwable throwable) {
		myEvent.add(new ConnectionLostEvent(throwable));
		myDoResubscribe = true;
	}

	@Override
	public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
		Message m = myMessageFactory.Create(topic, mqttMessage);
		if (m != null) {
			myMessage.push(m);
		}
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

	}

	public void subscribe(String topic, Message.QOS qos) {
		try {
			myClient.subscribe(topic, qos.getValue(), null, this, this);
			mySubscriptions.put(topic, qos);
		} catch (MqttException ex) {
			myLog.severe(ex.getMessage());
		}
	}

	@Override
	public void publish(String topic, byte[] payload, Message.QOS qos, boolean retained) {
		try {
			myLog.finest("Publish: " + topic + "[Q:" + qos + ", R:" + retained + "] " + Arrays.toString(payload));
			myClient.publish(topic, payload, qos.getValue(), retained, null, this);
		} catch (MqttException e) {
			myLog.severe(e.getMessage());
		}
	}

	@Override
	public void publish(Message m) {
		publish(m.getTopic(), m.getPayload(), m.getQos(), m.isRetained());
	}

	public IMqttAsyncClient getClient() {
		return myClient;
	}

	public IModuleRunner getStateProvider() {
		return myStateProvider;
	}

	public Logger getLogger() {
		return myLog;
	}

	public void startSingleShotTimer(Instant triggerTime, TimedEventBase event) {
		timedQueue.add(new TimedEvent(triggerTime, event));
	}

	@Override
	public void onSuccess(IMqttToken token) {
		myEvent.add(new SuccessEvent(token));
	}

	@Override
	public void onFailure(IMqttToken token, Throwable throwable) {
		myEvent.add(new FailureEvent(token, throwable));
	}

	private final ConcurrentLinkedQueue<EventBase> myEvent = new ConcurrentLinkedQueue<>();
	private final PriorityQueue<TimedEvent> timedQueue = new PriorityQueue<>(new TimedComparator());
	private final ConcurrentLinkedDeque<Message> myMessage = new ConcurrentLinkedDeque<>();
	private final HashMap<String, Message.QOS> mySubscriptions = new HashMap<>();

	private final MessageFactory myMessageFactory = new MessageFactory();
	private IMqttAsyncClient myClient = null;
	private final String myBroker;
	private final String myClientId;
	private final Logger myLog;
	private IModuleRunner myStateProvider;
	private boolean myDoResubscribe = false;

}
