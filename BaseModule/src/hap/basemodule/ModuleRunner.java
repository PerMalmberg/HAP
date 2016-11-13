// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.basemodule;

import cmdparser4j.CmdParser4J;
import cmdparser4j.IParseResult;
import cmdparser4j.SystemOutputParseResult;
import cmdparser4j.SystemOutputUsageFormatter;
import hap.LogFormatter;
import hap.SysUtil;
import hap.message.Message;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.*;

public class ModuleRunner {

	private final IHAPModule myModule;

	public ModuleRunner(IHAPModule module) {
		myModule = module;
		myLog = Logger.getLogger(module.getClass().getName());
		myResult = new SystemOutputParseResult();
		myParser = new CmdParser4J(myResult);
	}

	public boolean initialize(String[] args) {
		myParser.accept("--broker").asString(1).describedAs("The DNS or IP of the MQTT broker").setMandatory().withAlias("-b");
		myParser.accept("--topic").asString(1).describedAs("The root topic for this instance").setMandatory().withAlias("-r");
		myParser.accept("--working-dir").asString(1).describedAs("The working directory").withAlias("-w");
		myParser.accept("--module-dir").asString(1).describedAs("Directory containing modules").withAlias("-m");
		myParser.accept("--log-to-console").asSingleBoolean().describedAs("If specified, logging to console will be enabled").withAlias("-l");
		myParser.accept("--help").asSingleBoolean().describedAs("Print help text").withAlias("-?");

		Message.setTopicRoot(myParser.getString("--topic"));

		return setup(myLog, myParser, myResult, args);
	}

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
				myWorkDir = Paths.get(parser.getString("--working-dir", 0, Paths.get(SysUtil.getDirectoryOfJar(myModule.getClass()), "data").toAbsolutePath().toString()));
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

		return res;
	}

	public boolean run() {
		return false;
	}

	private IParseResult myResult;
	private CmdParser4J myParser;
	private Logger myLog;
	private String myBroker;
	protected Path myWorkDir;
}
