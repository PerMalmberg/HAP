// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.examplemodule;

import cmdparser4j.CmdParser4J;
import cmdparser4j.XMLConfigurationReader;
import hap.basemodule.ModuleRunner;
import hap.communication.Communicator;
import hap.communication.state.CommState;
import hap.examplemodule.state.ExampleModuleEntryState;

public class ExampleModule extends ModuleRunner
{

	public ExampleModule()
	{
		super(ExampleModule.class.getName());
	}

	public static void main( String... args )
	{
		ExampleModule m = new ExampleModule();

		int result = 1;

		if( m.initialize(args) )
		{
			result = m.run(m) ? 0 : 1;
		}

		System.exit(result);
	}

	@Override
	protected boolean initializeModule( CmdParser4J myParser )
	{
		// Read command line parameters and initialize module
		return true;
	}

	@Override
	protected void initCmdParser( CmdParser4J parser, XMLConfigurationReader configurationReader )
	{
		// Add more command line arguments here if needed by the module
	}

	public CommState createEntryState( Communicator com )
	{
		// Provide an entry state to your module that will be activated whenever the module
		// runner successfully connects. You should create a new instance, not reuse an previous instance.
		// If you want to retain state between state changes, then you should store that in the module itself.
		return new ExampleModuleEntryState(com);
	}
}
