package hap.modulemonitor;

import hap.message.MessageListener;
import hap.message.cmd.Ping;
import hap.message.general.UnclassifiedMessage;
import hap.message.response.PingResponse;

import java.time.Instant;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Logger;


public class ActiveModules extends MessageListener {

	@Override
	public void accept(PingResponse msg) {
		String mod = msg.getModuleName();
		myModules.put(mod, Instant.now());
		myLog.fine("Module reporting active: " + mod);
	}

	public void tick() {
		// Any module that hasn't checked in for fifteen seconds is considered dead.
		Instant threshold = Instant.now().minusSeconds(15);

		Set<String> strings = myModules.keySet();
		String[] mods = strings.toArray(new String[strings.size()]);
		for (String m : mods) {
			if (myModules.get(m).isBefore(threshold)) {
				myModules.remove(m);
				myLog.fine("Dead module: " + m);
			}
		}
	}

	public boolean isModuleActive(String name) {
		return myModules.containsKey(name);
	}

	private final HashMap<String, Instant> myModules = new HashMap<>();
	private static Logger myLog = Logger.getLogger("HAPCore");
}
