// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap;

import cmdparser4j.CmdParser4J;
import hap.basemodule.ModuleRunner;
import hap.communication.Communicator;
import hap.communication.state.CommState;
import hap.message.Message;
import hap.message.cmd.Start;
import hap.message.cmd.Stop;

import java.util.ArrayList;

public class Cli extends ModuleRunner {

	public static void main(String... args) {
		Cli m = new Cli();

		int result = 1;

		if (m.initialize(args)) {
			result = m.run(m) ? 0 : 1;
		}

		System.exit(result);
	}

	public Cli() {
		super(Cli.class.getName());
	}


	@Override
	protected boolean initializeModule(CmdParser4J parser) {
		// Read command line parameters and initialize module
		String moduleName = parser.getString( "--start-module");
		if( moduleName != null ) {
			Message m = new Start(moduleName);
			myCommands.add(m);
		}
		else {
			moduleName = parser.getString("--stop-module");
			if( moduleName != null ) {
				Message m = new Stop(moduleName );
				myCommands.add(m);
			}
		}

		return myCommands.size() > 0;
	}

	@Override
	protected void initCmdParser(CmdParser4J parser) {
		// Add more command line arguments here if needed by the module
		parser.accept("--start-module").asString(1).blockedBy("--stop-module").describedAs("Starts a module");
		parser.accept("--stop-module").asString(1).blockedBy("--start-module").describedAs("Stops a module");
	}

	@Override
	public CommState createEntryState(Communicator com) {
		// Provide an entry state to your module that will be activated whenever the module
		// runner successfully connects. You should create a new instance, not reuse an previous instance.
		// If you want to retain state between state changes, then you should store that in the module itself.
		return new ExecuteCommandState(com, myCommands, this);
	}

	private final ArrayList<Message> myCommands = new ArrayList<>();
}
