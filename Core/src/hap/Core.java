// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap;

import cmdparser4j.CmdParser4J;
import cmdparser4j.IParseResult;
import cmdparser4j.SystemOutputParseResult;
import cmdparser4j.SystemOutputUsageFormatter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.*;

public class Core {

	///////////////////////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////////////////////
	public Core(String[] args) {
		myArgs = args;
		myResult = new SystemOutputParseResult();
		myParser = new CmdParser4J(myResult);
		myParser.accept("--broker").asString(1).describedAs("The DNS or IP of the MQTT broker").setMandatory().withAlias("-b");
		myParser.accept("--topic").asString(1).describedAs("The root topic for this instance").setMandatory().withAlias("-r");
		myParser.accept("--working-dir").asString(1).describedAs("The working directory").withAlias("-w");
		myParser.accept("--module-dir").asString(1).describedAs("Directory containing modules").withAlias("-m");
		myParser.accept("--log-to-console").asSingleBoolean().describedAs("If specified, logging to console will be enabled").withAlias("-l");
		myParser.accept("--help").asSingleBoolean().describedAs("Print help text").withAlias("-?");
	}

	///////////////////////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////////////////////
	public static void main(String[] args) {
		Core c = new Core(args);
		int result = c.run();
		System.exit(result);
	}

	///////////////////////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////////////////////
	private int run() {
		boolean res = setup();

		if (res) {
			boolean done = false;
			ModuleMonitor mm = new ModuleMonitor( myWorkDir, myModDir, "HAPControl", myLog);

			mm.start();

			while (!done) {
				mm.tick();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					done = true;
				}
			}
		}

		return res ? 0 : 1;
	}

	private boolean setup() {
		// Disable default global logging handlers
		LogManager.getLogManager().reset();

		for (Handler h : Logger.getLogger("").getHandlers()) {
			Logger.getGlobal().removeHandler(h);
		}

		myLog = Logger.getLogger("HAPCore");
		ConsoleHandler console = new ConsoleHandler();
		myLog.addHandler(console);
		myLog.setLevel(Level.ALL);

		boolean res = myParser.parse(myArgs);

		if (res) {
			if (myParser.getBool("--help")) {
				SystemOutputUsageFormatter usage = new SystemOutputUsageFormatter("HAP::Core");
				myParser.getUsage(usage);
				myLog.info(usage.toString());
			}
			else {
				myWorkDir = Paths.get(myParser.getString("--working-dir", 0, Paths.get(SysUtil.getDirectoryOfJar(Core.class), "data").toAbsolutePath().toString()));
				myModDir = Paths.get(myParser.getString("--module-dir", 0, Paths.get(SysUtil.getDirectoryOfJar(Core.class), "modules").toAbsolutePath().toString()));

				if (Files.exists(myWorkDir)) {
					try {
						myLog.addHandler(new FileHandler(Paths.get(myWorkDir.toString(), "core.log").toString(), 1024 * 1024, 10, true));
					} catch (IOException e) {
						res = false;
						myLog.severe(e.getMessage());
					}
				} else {
					myLog.severe("Working directory does not exist (" + myWorkDir.toString() + ")");
					res = false;
				}

				if (!Files.exists(myModDir)) {
					myLog.severe("Module directory does not exist (" + myModDir.toString() + ")");
					res = false;
				}
			}
		} else {
			myLog.info(myResult.getParseResult());
		}

		if (!myParser.getBool("--log-to-console")) {
			myLog.removeHandler(console);
		}

		return res;
	}

	///////////////////////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////////////////////
//	private void LoadModules() {
//		// Find modules in the sub directory 'modules'
//		String workingDir = SysUtil.getDirectoryOfJar(Core.class);
//		Path fullPath = Paths.get(workingDir, "modules");
//		File[] modules = getModulesToLoad(fullPath.toString());
//		//return myMods.load(modules);
//	}

	///////////////////////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////////////////////
//	private File[] getModulesToLoad(String workingDir) {
//		List<String> modules = new ArrayList<>();
//
//		for (int i = 0; i < myParser.getAvailableStringParameterCount(myModuleArg); ++i) {
//			String modToLoad = myParser.getString(myModuleArg, i);
//			if (!modToLoad.endsWith(".jar")) {
//				modToLoad += ".jar";
//			}
//			modules.add(modToLoad);
//		}
//
//		File f = new File(workingDir);
//		return f.listFiles(o -> modules.contains(o.getName()));
//	}

	private Logger myLog;
	private String[] myArgs;
	private IParseResult myResult;
	private CmdParser4J myParser;
	private Path myModDir;
	private Path myWorkDir;
}
