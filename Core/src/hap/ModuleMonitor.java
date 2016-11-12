// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap;


import hap.state.ConnectState;
import hap.state.ModuleMonitorFSM;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.nio.file.Path;
import java.util.logging.Logger;

public class ModuleMonitor {

	public ModuleMonitor(Path workingDir, Path moduleDir, String topicRoot, Logger log, String broker) {
		myWorkingDir = workingDir;
		myModuleDir = moduleDir;
		myTopicRoot = topicRoot;
		myLog = log;
		myBroker = broker;
	}

	public void tick() {
		myFsm.tick();
	}

	public boolean start() {
		boolean res = true;

		try {
			myFsm = new ModuleMonitorFSM(new MqttAsyncClient(myBroker, "HAPCore-" + myTopicRoot, new MemoryPersistence()), myTopicRoot, myLog, myWorkingDir, myModuleDir);
			myFsm.setState(new ConnectState(myFsm));
		} catch (MqttException e) {
			myLog.severe(e.getMessage());
			res = false;
		}

		return res;
	}


	private ModuleMonitorFSM myFsm;
	private final String myTopicRoot;
	private final Path myWorkingDir;
	private final Path myModuleDir;
	private final Logger myLog;
	private final String myBroker;
}
