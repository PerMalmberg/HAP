// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap;

import cmdparser4j.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
		myParser.accept(myModuleArg).asString(1, Constructor.NO_PARAMETER_LIMIT ).describedAs("Modules to load").setMandatory().withAlias("-m");

		myMods = new ModuleMonitor();
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
		boolean res = myParser.parse(myArgs);

		if (!res) {
			System.out.println(myResult.getParseResult());
			SystemOutputUsageFormatter usage = new SystemOutputUsageFormatter("HAP::Core");
			myParser.getUsage(usage);
			System.out.println(usage.toString());
		}
		else {
			res = LoadModules();
		}

		return res ? 0 : 1;
	}

	///////////////////////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////////////////////
	private boolean LoadModules() {
		// Find modules in the sub directory 'modules'
		String workingDir = SysUtil.getDirectoryOfJar(Core.class);
		Path fullPath = Paths.get( workingDir, "modules" );
		File[] modules = getModulesToLoad( fullPath.toString() );
		return myMods.load( modules );
	}

	///////////////////////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////////////////////
	private File[] getModulesToLoad( String workingDir ) {
		List<String> modules = new ArrayList<>();

		for( int i = 0; i < myParser.getAvailableStringParameterCount(myModuleArg); ++i )
		{
			String modToLoad = myParser.getString(myModuleArg, i);
			if( !modToLoad.endsWith(".jar")) {
				modToLoad += ".jar";
			}
			modules.add( modToLoad );
		}

		File f = new File(workingDir);
		return f.listFiles( o -> modules.contains( o.getName()  ) );
	}

	private String[] myArgs;
	private IParseResult myResult;
	private CmdParser4J myParser;
	private ModuleMonitor myMods;
	private String myModuleArg = "--module";
}
