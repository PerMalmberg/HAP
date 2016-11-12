package hap.state;


import chainedfsm.FSM;
import hap.event.EventBase;
import hap.event.FailureEvent;
import hap.event.MessageEvent;
import hap.event.SuccessEvent;
import org.eclipse.paho.client.mqttv3.*;

import java.nio.file.Path;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

public class ModuleMonitorFSM extends FSM<ModuleMonitorStateBase> implements IMqttActionListener, IMqttMessageListener {

	public ModuleMonitorFSM(IMqttAsyncClient client, String topicRoot, Logger logger, Path workingDir, Path moduleDir) {
		myClient = client;
		myTopicRoot = topicRoot;
		myLog = logger;
		myWorkingDir = workingDir;
		mymoduleDir = moduleDir;
	}

	@Override
	public void onSuccess(IMqttToken token) {
		myEvent.add(new SuccessEvent(token));
	}

	@Override
	public void onFailure(IMqttToken token, Throwable throwable) {
		myEvent.add(new FailureEvent(token, throwable));
	}

	@Override
	public void messageArrived(String s, MqttMessage message) {
		myEvent.add(new MessageEvent(s, message));
	}


	public IMqttAsyncClient getClient() {
		return myClient;
	}

	public void tick() {
		while (!myEvent.isEmpty()) {
			EventBase e = myEvent.poll();
			e.visit(getCurrentState());
		}

		TimedEvent te = timedQueue.peek();
		while( te != null && te.getInstant().isBefore( Instant.now() ) ) {
			te = timedQueue.poll();
			te.getEvent().visit( getCurrentState() );
			// Get next possible event
			te = timedQueue.peek();
		}

		getCurrentState().tick();
	}

	public java.util.logging.Logger getLogger() {
		return myLog;
	}

	public String getTopicRoot() {
		return myTopicRoot;
	}

	private IMqttAsyncClient myClient;
	private final String myTopicRoot;
	private java.util.logging.Logger myLog;
	private final Path myWorkingDir;
	private final Path mymoduleDir;
	private final ConcurrentLinkedQueue<EventBase> myEvent = new ConcurrentLinkedQueue<>();
	private final PriorityQueue<TimedEvent> timedQueue = new PriorityQueue<>(new TimedComparator());

	public Path getWorkingDir() {
		return myWorkingDir;
	}

	public Path getModuleDir() {
		return mymoduleDir;
	}

	public void startSingleShotTimer(Instant triggerTime, EventBase event) {
		timedQueue.add(new TimedEvent(triggerTime, event));
	}
}
