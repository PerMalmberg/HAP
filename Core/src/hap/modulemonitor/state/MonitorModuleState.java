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
import java.io.InputStream;
import java.nio.file.Path;
import java.time.Instant;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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
			try {
				ZipFile zip = new ZipFile(f);
				ZipEntry manifest = zip.getEntry("META-INF/MANIFEST.MF");
				if (manifest == null) {
					myLog.warning("No META-INF/MANIFEST.MF found in " + f.getAbsolutePath());
				} else {
					InputStream zin = zip.getInputStream(manifest);
					Manifest m = new Manifest(zin);
					Attributes attributes = m.getMainAttributes();
					String moduleName = attributes.getValue("Main-Class");
					if (moduleName == null) {
						myLog.warning("No Main-Class in manifest of " + f.getAbsolutePath());
					} else if (!myModules.isModuleActive(moduleName)
							&& myModules.mayStart(moduleName)) {

						ProcessBuilder pb = new ProcessBuilder();

						pb.command("java", "-Xms20m",
								"-jar",
								f.getAbsolutePath(),
								"-w", myWorkingDir.toString(),
								"--broker", myCom.getClient().getServerURI(),
								"--topic", Message.getTopicRoot(),
								"-l");

						pb.inheritIO();

						try {
							myLog.finest("Starting module '" + moduleName + "'.");
							Process p = pb.start();
							myModules.update(moduleName, p);
						} catch (IOException e) {
							myLog.severe(e.getMessage());
							myModules.delayStart(moduleName);
						}
					}

				}

				zip.close();
			} catch (IOException e) {
				myLog.severe("Error while loading module: " + e.getMessage());
			}
		}
	}

	@Override
	public void tick() {
		myModules.tick();
	}

	@Override
	public void accept(MessageEvent e) {
		Message m = myMessageFactory.Create(e.getTopic(), e.getMsg());
		if (m != null) {
			m.visit(myModules);
		}
	}

	@Override
	public void accept(PingResponse msg) {
		msg.visit(myModules);
	}

	@Override
	public void accept(PingTimeoutEvent e) {
		// Ping has timed out, check what modules are running and which ones that needs to be started.
		startModules();

		sendPing();
	}

	private final MessageFactory myMessageFactory = new MessageFactory();
	private final ActiveModules myModules = new ActiveModules();
	private final Logger myLog;
	private final Path myModuleDir;
	private final Path myWorkingDir;

}
