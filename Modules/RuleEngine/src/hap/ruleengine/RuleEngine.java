// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine;

import cmdparser4j.CmdParser4J;
import cmdparser4j.XMLConfigurationReader;
import hap.basemodule.ModuleRunner;
import hap.communication.Communicator;
import hap.communication.state.CommState;
import hap.ruleengine.state.LoadRules;

public class RuleEngine extends ModuleRunner
{
	public RuleEngine()
	{
		super( RuleEngine.class.getName());
	}

	public static void main( String... args )
	{
		RuleEngine m = new RuleEngine();

		int result = 1;

		if( m.initialize( args ) )
		{
			result = m.run( m ) ? 0 : 1;
		}

		System.exit( result );
	}

	@Override
	protected boolean initializeModule( CmdParser4J myParser )
	{
		return true;
	}

	@Override
	protected void initCmdParser( CmdParser4J parser, XMLConfigurationReader configurationReader )
	{

	}

	@Override
	public CommState createEntryState( Communicator com )
	{
		return new LoadRules( com );
	}
}
