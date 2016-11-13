// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.basemodule;

import cmdparser4j.CmdParser4J;
import cmdparser4j.IParseResult;
import cmdparser4j.SystemOutputParseResult;
import cmdparser4j.SystemOutputUsageFormatter;
import hap.LogFormatter;
import hap.SysUtil;
import hap.basemodule.communication.Communicator;
import hap.message.IMessageListener;
import hap.message.Message;
import hap.message.response.PingResponse;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.*;

public abstract class ModuleRunner implements IMessageListener {

	public ModuleRunner(String moduleName ) {
		myModuleName = moduleName;
		myLog = Logger.getLogger(moduleName);
		myResult = new SystemOutputParseResult();
		myParser = new CmdParser4J(myResult);
	}

	public boolean initialize(String[] args) {
		myParser.accept("--broker").asString(1).describedAs("The DNS or IP of the MQTT broker").setMandatory().withAlias("-b");
		myParser.accept("--topic").asString(1).describedAs("The root topic used by the HAP Core").setMandatory().withAlias("-r");
		myParser.accept("--working-dir").asString(1).describedAs("The working directory").withAlias("-w");
		myParser.accept("--module-dir").asString(1).describedAs("Directory containing modules").withAlias("-m");
		myParser.accept("--log-to-console").asSingleBoolean().describedAs("If specified, logging to console will be enabled").withAlias("-l");
		myParser.accept("--help").asSingleBoolean().describedAs("Print help text").withAlias("-?");

		initCmdParser( myParser );
		return setup(myLog, myParser, myResult, args);
	}

	// Overridden by the module and used to read command line arguments.
	protected abstract boolean initializeModule(CmdParser4J myParser);

	// Overridden by the module and used to setup additional command line arguments.
	protected abstract void initCmdParser(CmdParser4J parser);

	public boolean setup(Logger logger, CmdParser4J parser, IParseResult result, String... args) {
		// Disable default global logging handlers
		LogManager.getLogManager().reset();

		for (Handler h : Logger.getLogger("").getHandlers()) {
			Logger.getGlobal().removeHandler(h);
		}

		Level level = Level.FINEST;
		logger.setLevel(level);
		ConsoleHandler console = new ConsoleHandler();
		console.setFormatter(new LogFormatter());
		console.setLevel(level);
		logger.addHandler(console);

		boolean res = parser.parse(args);

		if (res) {
			if (parser.getBool("--help")) {
				SystemOutputUsageFormatter usage = new SystemOutputUsageFormatter("HAP::Core");
				parser.getUsage(usage);
				logger.info(usage.toString());
			} else {
				myWorkDir = Paths.get(parser.getString("--working-dir", 0, Paths.get(SysUtil.getDirectoryOfJar(ModuleRunner.class), "data").toAbsolutePath().toString()));
				myBroker = parser.getString("--broker");

				if (Files.exists(myWorkDir)) {
					try {
						FileHandler fh = new FileHandler(Paths.get(myWorkDir.toString(), "HAPCore.log").toString(), 1024 * 1024, 10, true);
						fh.setFormatter(new LogFormatter());
						fh.setLevel(level);
						logger.addHandler(fh);
					} catch (IOException e) {
						res = false;
						logger.severe(e.getMessage());
					}
				} else {
					logger.severe("Working directory does not exist (" + myWorkDir.toString() + ")");
					res = false;
				}

				try {
					URI uri = new URI(parser.getString("--broker"));
					if (uri.getScheme() == null) {
						logger.severe("Broker must be specified with URI scheme, i.e. tcp://<DNS|IP>");
						res = false;
					}
				} catch (URISyntaxException e) {
					logger.severe("Invalid broker specified");
					res = false;
				}
			}
		} else {
			logger.info(result.getParseResult());
		}

		if (!parser.getBool("--log-to-console")) {
			logger.removeHandler(console);
		}

		Message.setTopicRoot(myParser.getString("--topic"));

		if( res ) {
			// Let the module initialize
			res = initializeModule(myParser);
		}

		return res;
	}

	public boolean run() {
		myCom = new Communicator(myBroker, myModuleName, this, myLog);
		myCom.start();

		try {
			while (!myIsTerminated) {
				Thread.sleep(0, 1);
				myCom.tick();
				tick();
			}
		} catch (InterruptedException e) {
			myLog.finest(e.getMessage());
		}

		return myIsTerminated;
	}

	protected void terminate() {
		myIsTerminated = true;
	}

	protected void publish(Message message) {
		myCom.publish( message.getTopic(), message.getPayload(), message.getQos(), message.isRetained() );
	}

	protected abstract void tick();

	@Override
	public void accept(PingResponse msg) {
		// Not used by modules.
	}

	private Communicator myCom = null;
	private final String myModuleName;
	private IParseResult myResult;
	private CmdParser4J myParser;
	private Logger myLog;
	private String myBroker;
	protected Path myWorkDir;
	private boolean myIsTerminated = false;
}
