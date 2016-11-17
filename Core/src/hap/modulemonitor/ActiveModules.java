package hap.modulemonitor;

import hap.message.MessageListener;
import hap.message.response.PingResponse;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Logger;


public class ActiveModules extends MessageListener {

	@Override
	public void accept(PingResponse msg) {
		String mod = msg.getModuleName();

		ModuleContainer m = myModules.get(msg.getModuleName());
		if (m == null) {
			myLog.warning("Received ping response from unmonitored module: '" + mod + "'");
		} else {
			m.lastLifesign = Instant.now();
		}
		myLog.finest("Module reporting active: " + mod);
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
				module.process.destroyForcibly();
				delayStart(moduleName);
			} else if (module.hasTerminated()) {
				delayStart(moduleName);
			}
		}
	}

	public void update(String moduleName, Process p) {
		ModuleContainer m = myModules.get(moduleName);
		if (m == null) {
			myModules.put(moduleName, new ModuleContainer(Instant.now(), p));
		} else {
			m.process = p;
			m.lastLifesign = Instant.now();
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

	private final HashMap<String, ModuleContainer> myModules = new HashMap<>();
	private static Logger myLog = Logger.getLogger("HAPCore");

	public void delayStart(String moduleName) {
		ModuleContainer m = myModules.get(moduleName);
		if (m == null) {
			m = new ModuleContainer(Instant.now());
		}

		m.process = null;
		myLog.info("Preventing automatic restart of module until 15 minutes from now");
		m.doNotStartBefore = Instant.now().plus(15, ChronoUnit.MINUTES);
	}
}
