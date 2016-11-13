// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

import cmdparser4j.CmdParser4J;
import hap.basemodule.ModuleRunner;
import hap.message.cmd.Ping;
import hap.message.general.UnclassifiedMessage;
import hap.message.response.PingResponse;

public class ExampleModule extends ModuleRunner {

	public static void main(String... args) {
		ExampleModule m = new ExampleModule();

		if (m.initialize(args)) {
			System.exit(m.run() ? 0 : 1);
		} else {
			System.exit(1);
		}
	}

	public ExampleModule() {
		super(ExampleModule.class.getName());
	}

	@Override
	protected boolean initializeModule(CmdParser4J myParser) {
		// Initialize the module; subscribe to topics etc.
		return true;
	}

	@Override
	protected void initCmdParser(CmdParser4J parser) {
		// Add more command line arguments here if needed by the module
	}

	@Override
	protected void tick() {
		// Called periodically. Don't do long running task here.
	}

	@Override
	public void accept(Ping msg) {
		// We received a ping for the Core, respond with our name
		publish( new PingResponse(ExampleModule.class.getName()));
	}

	@Override
	public void accept(UnclassifiedMessage msg) {
		// This is where any unclassified messages (i.e. not a control message) are received.
	}
}
