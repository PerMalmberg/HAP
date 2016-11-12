package hap.state;

import chainedfsm.EnterLeaveState;
import hap.event.IEventListener;

public abstract class ModuleMonitorStateBase extends EnterLeaveState implements IEventListener {
	public ModuleMonitorStateBase(ModuleMonitorFSM fsm) {
		myFsm = fsm;
	}

	protected String combineTopic(String topicRoot, String topic) {
		String t = topicRoot;
		if (!topicRoot.endsWith("/")) {
			t += "/";
		}

		if (topic.startsWith("/")) {
			topic = topic.substring(1);
		}

		t += topic;
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


}
