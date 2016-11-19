package hap.modulemonitor;

import hap.communication.IPublisher;
import hap.message.MessageListener;
import hap.message.cmd.Start;
import hap.message.cmd.Stop;
import hap.message.response.PingResponse;
import hap.message.response.StartResponse;
import hap.message.response.StopResponse;

import java.time.Instant;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Logger;


public class ActiveModules extends MessageListener {

	public ActiveModules(IPublisher publisher) {
		myPublisher = publisher;
	}

	@Override
	public void accept(PingResponse msg) {
		String mod = msg.getModuleName();

		ModuleContainer m = myModules.get(msg.getModuleName());
		if (m == null) {
			myLog.warning("Received ping response from unmonitored module: '" + mod + "'");
		} else {
			m.setIsAlive();
		}
		myLog.finest("Module reporting active: " + mod);
	}

	@Override
	public void accept(Stop msg) {
		String name = new String(msg.getPayload());
		ModuleContainer mod = myModules.get(name);
		if (mod == null) {
			myPublisher.publish(new StopResponse(name, false));
			myLog.warning("Received message to stop module '" + name + "' but no such module is known.");
		} else if (mod.isActive()) {
			mod.terminate();
			mod.disableAutoStart();
			myPublisher.publish(new StopResponse(name, true));
			myLog.info("Stopped module '" + name + "'");
		}
	}

	@Override
	public void accept(Start msg) {
		String name = new String(msg.getPayload());
		myLog.finest("Received message to start module '" + name + "'");

		ModuleContainer mod = myModules.get(name);
		if (mod != null && !mod.isActive() ) {
			mod.enableAutoStart();
		}
	}

	public void tick() {
		// Any module that hasn't checked in for fifteen seconds is considered dead and shall be terminated.
		Instant threshold = Instant.now().minusSeconds(15);

		Set<String> strings = myModules.keySet();
		String[] mods = strings.toArray(new String[strings.size()]);
		for (String moduleName : mods) {
			ModuleContainer module = myModules.get(moduleName);

			if (module.isActive() && module.hasExpired(threshold)) {
				myLog.warning("Terminating process for expired module '" + moduleName + "'");
				module.terminate();
			} else if (module.hasTerminated()) {
				myLog.warning("Delaying restart of module '" + moduleName + "'");
				module.delayStart();
			}
		}
	}

	public void update(String moduleName, Process p) {
		ModuleContainer m = myModules.get(moduleName);
		if (m == null) {
			myModules.put(moduleName, new ModuleContainer(Instant.now(), p));
		} else {
			m.setProcess(p);
		}
	}

	public boolean mayStart(String name) {
		boolean mayStart = true;

		ModuleContainer m = myModules.get(name);
		if (m != null) {
			mayStart = m.mayStart();
		}

		return mayStart;
	}

	public boolean isModuleActive(String name) {
		boolean active = false;

		ModuleContainer m = myModules.get(name);
		if (m != null) {
			active = m.isActive();
		}

		return active;
	}

	public void delayStart(String moduleName) {
		ModuleContainer m = myModules.get(moduleName);
		if (m == null) {
			m = new ModuleContainer(Instant.now());
			myModules.put(moduleName, m);
		}

		m.delayStart();
	}

	public void killAll() {
		for (ModuleContainer m: myModules.values() ) {
			m.terminate();
		}
	}
	
	private final IPublisher myPublisher;
	private final HashMap<String, ModuleContainer> myModules = new HashMap<>();
	private static Logger myLog = Logger.getLogger("HAPCore");

}
