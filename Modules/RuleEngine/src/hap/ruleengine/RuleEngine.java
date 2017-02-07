// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine;

import cmdparser4j.CmdParser4J;
import cmdparser4j.XMLConfigurationReader;
import hap.basemodule.ModuleRunner;
import hap.communication.Communicator;
import hap.communication.state.CommState;
import hap.ruleengine.parts.composite.CompositeComponent;
import hap.ruleengine.state.LoadRules;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class RuleEngine extends ModuleRunner
{
	private RuleEngine()
	{
		super( RuleEngine.class.getName());
	}
	private HashMap<UUID, CompositeComponent> loadedSchema = new HashMap<>();
	private CmdParser4J myParser;

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
	protected boolean initializeModule( @NotNull CmdParser4J parser )
	{
		myParser = parser;
		return true;
	}

	@Override
	protected void initCmdParser( @NotNull CmdParser4J parser, @NotNull XMLConfigurationReader configurationReader )
	{
	}

	@Override
	public CommState createEntryState( Communicator com )
	{
		return new LoadRules( loadedSchema, myParser, com );
	}
}
