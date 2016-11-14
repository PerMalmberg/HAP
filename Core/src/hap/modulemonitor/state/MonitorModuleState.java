package hap.modulemonitor.state;

import chainedfsm.EnterChain;
import hap.communication.Communicator;
import hap.communication.state.CommState;
import hap.event.MessageEvent;
import hap.event.PingTimeoutEvent;
import hap.message.Message;
import hap.message.MessageFactory;
import hap.message.cmd.Ping;
import hap.message.response.PingResponse;
import hap.modulemonitor.ActiveModules;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.logging.Logger;

public class MonitorModuleState extends CommState {


	public MonitorModuleState(Communicator com, Path moduleDir, Path workingDir) {
		super(com);
		myLog = com.getLogger();
		myModuleDir = moduleDir;
		myWorkingDir = workingDir;
		new EnterChain<>(this, this::enter);
	}

	private void enter() {
		sendPing();
	}

	private void sendPing() {
		// Send a ping to see which modules that are alive already and setup a timeout
		publish(new Ping());
		myCom.startSingleShotTimer(Instant.now().plusSeconds(3), new PingTimeoutEvent());
	}

	private void startModules() {
		File wDir = myModuleDir.toFile();
		File[] modules = wDir.listFiles((dir, name) -> {
			return name.endsWith(".jar");
		});

		for (File f : modules) {
			// Find name of main class in META-INF/MANIFEST.MF in the jar file.
			// That name will be what we expect from in the PingResponse from the module.

			String modName = getModName( f );
			if( !myActiveModules.isModuleActive( modName )) {
				// Start module
				ProcessBuilder pb = new ProcessBuilder();
				pb.command( "java", "-Xms20m -jar " + f.getAbsolutePath() + "-w " + myWorkingDir );
// QQQ				try {
//					pb.start();
//				} catch (IOException e) {
//					myLog.severe(e.getMessage());
//				}
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
	public void accept(PingResponse msg) {
		msg.visit(myActiveModules);
	}

	@Override
	public void accept(PingTimeoutEvent e) {
		// Ping has timed out, check what modules are running and which ones that needs to be started.
		startModules();

		sendPing();
	}

	private final MessageFactory myMessageFactory = new MessageFactory();
	private final ActiveModules myActiveModules = new ActiveModules();
	private final Logger myLog;
	private final Path myModuleDir;
	private final Path myWorkingDir;

}
