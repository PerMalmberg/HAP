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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.time.Instant;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

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
				ZipFile zip = new ZipFile( f );
				ZipEntry manifest = zip.getEntry("META-INF/MANIFEST.MF");
				if( manifest == null) {
					myLog.warning("No META-INF/MANIFEST.MF found in " + f.getAbsolutePath());
				}
				else {
					InputStream zin = zip.getInputStream(manifest);
					Manifest m = new Manifest(zin);
					Attributes attributes = m.getMainAttributes();
					String mainClass = attributes.getValue("Main-Class");
					if( mainClass == null ) {
						myLog.warning("No Main-Class in manifest of " + f.getAbsolutePath() );
					}
					else {
						if( myActiveModules.isModuleActive(mainClass)) {
							myLog.finest("Module '" + mainClass + "' is already active");
						}
						else {
							myLog.fine("Attempting to load module '" + mainClass + "' from " + f.getAbsolutePath());
							myActiveModules.prepareModule( mainClass );
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
								// TODO: Save process with its entry in myModules for later monitoring and termination.
								Process p = pb.start();
								int i = 0;
							} catch (IOException e) {
								myLog.severe(e.getMessage());
							}
						}
					}
				}

				zip.close();
			} catch (IOException e) {
				myLog.severe( "Error while loading module: " + e.getMessage() );
			}

			// Find name of main class in META-INF/MANIFEST.MF in the jar file.
			// That name will be what we expect from in the PingResponse from the module.

//			String modName = getModName( f );
//
//				// Start module
//				ProcessBuilder pb = new ProcessBuilder();
//				pb.command( "java", "-Xms20m -jar " + f.getAbsolutePath() + "-w " + myWorkingDir );
//// QQQ				try {
////					pb.start();
////				} catch (IOException e) {
////					myLog.severe(e.getMessage());
////				}
//			}
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
