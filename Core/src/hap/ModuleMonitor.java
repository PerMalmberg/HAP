// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.logging.Logger;

public class ModuleMonitor implements IMqttActionListener, IMqttMessageListener {

	public ModuleMonitor(Path workingDir, Path moduleDir, String topicRoot, Logger log ) {
		myWorkingDir = workingDir;
		myModuleDir = moduleDir;
		myTopicRoot = topicRoot;
		myLog = log;
	}

	public void tick() {

	}

	public void start()
	{
		connect();
	}


	private void connect()
	{
		String content      = "Message from MqttPublishSample";
		int qos             = 2;
		String broker       = "tcp://192.168.10.245:1883";
		String clientId     = "HAPCore";
		MemoryPersistence persistence = new MemoryPersistence();

		try {
			myClient = new MqttAsyncClient(broker, clientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			connOpts.setAutomaticReconnect(true);
			myToken = myClient.connect( null, this );

		} catch(MqttException me) {
			System.out.println("reason "+me.getReasonCode());
			System.out.println("msg "+me.getMessage());
			System.out.println("loc "+me.getLocalizedMessage());
			System.out.println("cause "+me.getCause());
			System.out.println("excep "+me);
			me.printStackTrace();
		}
	}

	private final ArrayList<String> activeModules = new ArrayList<>();
	private final String myTopicRoot;
	private MqttAsyncClient myClient;
	private IMqttToken myToken;
	private final Path myWorkingDir;
	private final Path myModuleDir;
	private final Logger myLog;

	@Override
	public void onSuccess(IMqttToken iMqttToken) {
		try {
			// FSM - connect - subscribe - monitor
			 myClient.subscribe( combineTopic( myTopicRoot, "#" ), 2, null, this, this );
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	private String combineTopic(String topicRoot, String topic ) {
		String t = topicRoot;
		if( !topicRoot.endsWith("/")) {
			t += "/";
		}

		if( topic.startsWith("/")) {
			topic = topic.substring(1);
		}

		t += topic;
		return t;
	}

	@Override
	public void onFailure(IMqttToken iMqttToken, Throwable throwable) {

	}

	@Override
	public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {

	}
}
