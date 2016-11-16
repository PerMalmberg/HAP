// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.modulemonitor;


import hap.communication.Communicator;
import hap.communication.IModuleRunner;
import hap.communication.state.CommState;
import hap.message.Message;
import hap.modulemonitor.state.MonitorModuleState;

import java.nio.file.Path;
import java.util.logging.Logger;

public class ModuleMonitor implements IModuleRunner {

	public ModuleMonitor(Path workingDir, Path moduleDir, String broker) {
		myWorkingDir = workingDir;
		myModuleDir = moduleDir;
		myBroker = broker;
	}

	public boolean tick() {
		myCom.tick();
		return !myIsTerminated;
	}

	public boolean start() {
		myLog.info("Starting Module Monitor");
		myCom = new Communicator(myBroker, "HAPCore-" + Message.getTopicRoot(), myLog);
		return myCom.start(this);
	}

	@Override
	public CommState createEntryState(Communicator com) {
		return new MonitorModuleState(com, myModuleDir, myWorkingDir);
	}

	@Override
	public void terminate() {
		myIsTerminated = true;
	}

	private Communicator myCom;
	private final Path myWorkingDir;
	private final Path myModuleDir;
	private final String myBroker;
	private boolean myIsTerminated = false;

	private Logger myLog = Logger.getLogger("HAPCore");

}
