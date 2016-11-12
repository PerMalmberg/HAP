package hap.state;

import chainedfsm.EnterChain;
import hap.event.MessageEvent;
import hap.event.PingTimeoutEvent;

import java.io.File;
import java.time.Instant;

public class MonitorModuleState extends ModuleMonitorStateBase {

	public MonitorModuleState(ModuleMonitorFSM fsm) {
		super(fsm);

		new EnterChain<>(this, this::enter);
	}

	private void enter() {
		// Send a ping to see which modules that are alive already
		command("Ping", "*");
		myFsm.startSingleShotTimer(Instant.now().plusSeconds(3), new PingTimeoutEvent());
	}

	private void startModules() {
		File wDir = myFsm.getModuleDir().toFile();
		File[] modules = wDir.listFiles((dir, name) -> {
			return name.endsWith(".jar");
		});

		for (File f : modules) {

		}
	}

	@Override
	public void accept(PingTimeoutEvent e) {

	}


	@Override
	public void accept(MessageEvent e) {

	}
}
