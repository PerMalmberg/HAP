package hap.modulemonitor.state;

import chainedfsm.EnterChain;
import hap.event.MessageEvent;
import hap.event.PingTimeoutEvent;
import hap.message.Message;
import hap.message.MessageFactory;
import hap.message.cmd.Ping;
import hap.modulemonitor.ActiveModules;

import java.io.File;
import java.time.Instant;

public class MonitorModuleState extends ModuleMonitorStateBase {

	public MonitorModuleState(ModuleMonitorFSM fsm) {
		super(fsm);

		new EnterChain<>(this, this::enter);
	}

	private void enter() {
		sendPing();
	}

	private void sendPing() {
		// Send a ping to see which modules that are alive already and setup a timeout
		publish(new Ping());
		myFsm.startSingleShotTimer(Instant.now().plusSeconds(3), new PingTimeoutEvent());
	}

	private void startModules() {
		File wDir = myFsm.getModuleDir().toFile();
		File[] modules = wDir.listFiles((dir, name) -> {
			return name.endsWith(".jar");
		});

		for (File f : modules) {
			String modName = getModName( f );
			if( !myActiveModules.isModuleActive( modName )) {
				// Start module

			}
		}
	}

	private String getModName(File f) {
		String name = f.getName();
		return name.substring( 0, name.indexOf('.') );
	}

	@Override
	public void tick() {
		myActiveModules.tick();
	}

	@Override
	public void accept(MessageEvent e) {
		Message m = myMessageFactory.Create(e.getTopic(), e.getMsg());
		if (m != null) {
			m.visit(myActiveModules);
		}
	}

	@Override
	public void accept(PingTimeoutEvent e) {
		// Ping has timed out, check what modules are running and which ones that needs to be started.
		startModules();

		sendPing();
	}

	private final MessageFactory myMessageFactory = new MessageFactory();
	private final ActiveModules myActiveModules = new ActiveModules();

}
