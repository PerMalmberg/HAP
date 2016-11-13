// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.modulemonitor;


import hap.message.Message;
import hap.modulemonitor.state.ConnectState;
import hap.modulemonitor.state.ModuleMonitorFSM;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.nio.file.Path;
import java.util.logging.Logger;

public class ModuleMonitor {

	public ModuleMonitor(Path workingDir, Path moduleDir, String broker) {
		myWorkingDir = workingDir;
		myModuleDir = moduleDir;
		myBroker = broker;
	}

	public void tick() {
		myFsm.tick();
	}

	public boolean start() {
		boolean res = true;

		myLog.info("Starting Module Monitor");

		try {
			myFsm = new ModuleMonitorFSM(new MqttAsyncClient(myBroker, "HAPCore-" + Message.getTopicRoot(), new MemoryPersistence()), myWorkingDir, myModuleDir);
			myFsm.setState(new ConnectState(myFsm));
		} catch (MqttException e) {
			myLog.severe(e.getMessage());
			res = false;
		}

		return res;
	}


	private ModuleMonitorFSM myFsm;
	private final Path myWorkingDir;
	private final Path myModuleDir;
	private final String myBroker;

	private Logger myLog = Logger.getLogger("HAPCore");
}
