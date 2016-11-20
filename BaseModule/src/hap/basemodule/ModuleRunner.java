// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.basemodule;

import cmdparser4j.*;
import hap.LogFormatter;
import hap.SysUtil;
import hap.communication.Communicator;
import hap.communication.IModuleRunner;
import hap.message.Message;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.*;

public abstract class ModuleRunner implements IModuleRunner
{

public ModuleRunner( String moduleName )
{
	myModuleName = moduleName;
	myLog = Logger.getLogger( moduleName );
	myResult = new SystemOutputParseResult();
	myParser = new CmdParser4J( myResult );
}

public boolean initialize( String[] args )
{
	myParser.accept( "--broker" ).asString( 1 ).describedAs( "The DNS or IP of the MQTT broker" ).setMandatory().withAlias( "-b" );
	myParser.accept( "--topic" ).asString( 1 ).describedAs( "The root topic used by the HAP Core" ).setMandatory().withAlias( "-r" );
	myParser.accept( "--config" ).asString( 1 ).describedAs( "Full path to configuration file" ).withAlias( "-c" ).setMandatory();
	myParser.accept( "--working-dir" ).asString( 1 ).describedAs( "The working directory" ).withAlias( "-w" );
	myParser.accept( "--log-to-console" ).asSingleBoolean().describedAs( "If specified, logging to console will be enabled" ).withAlias( "-l" );
	myParser.accept( "--help" ).asSingleBoolean().describedAs( "Print help text" ).withAlias( "-?" );
	myCfg = new XMLConfigurationReader( myResult );

	initCmdParser( myParser, myCfg );

	boolean res = setup( myParser, myResult, args );
	if( res )
	{
		myCom = new Communicator( myBroker, myModuleName, myLog );
	}

	return res;
}

// Overridden by the module and used to read command line arguments.
protected abstract boolean initializeModule( CmdParser4J myParser );

// Overridden by the module and used to setup additional command line arguments.
protected abstract void initCmdParser( CmdParser4J parser, XMLConfigurationReader configurationReader );

private boolean setup( CmdParser4J parser, IParseResult result, String... args )
{
	// Disable default global logging handlers
	LogManager.getLogManager().reset();

	for( Handler h : Logger.getLogger( "" ).getHandlers() )
	{
		Logger.getGlobal().removeHandler( h );
	}

	Level level = Level.FINEST;
	myLog.setLevel( level );
	ConsoleHandler console = new ConsoleHandler();
	console.setFormatter( new LogFormatter() );
	console.setLevel( level );
	myLog.addHandler( console );

	boolean res = parser.parse( "--config", myCfg, args );

	if( res )
	{
		if( parser.getBool( "--help" ) )
		{
			SystemOutputUsageFormatter usage = new SystemOutputUsageFormatter( myModuleName );
			parser.getUsage( usage );
			myLog.info( usage.toString() );
		} else
		{
			myWorkDir = Paths.get( parser.getString( "--working-dir", 0, Paths.get( SysUtil.getDirectoryOfJar( ModuleRunner.class ), "data" ).toAbsolutePath().toString() ) );
			myBroker = parser.getString( "--broker" );

			if( Files.exists( myWorkDir ) )
			{
				try
				{
					FileHandler fh = new FileHandler( Paths.get( myWorkDir.toString(), myModuleName + ".log" ).toString(), 1024 * 1024, 10, true );
					fh.setFormatter( new LogFormatter() );
					fh.setLevel( level );
					myLog.addHandler( fh );
				}
				catch( IOException e )
				{
					res = false;
					myLog.severe( e.getMessage() );
				}
			} else
			{
				myLog.severe( "Working directory does not exist (" + myWorkDir.toString() + ")" );
				res = false;
			}

			try
			{
				URI uri = new URI( parser.getString( "--broker" ) );
				if( uri.getScheme() == null )
				{
					myLog.severe( "Broker must be specified with URI scheme, i.e. tcp://<DNS|IP>" );
					res = false;
				}
			}
			catch( URISyntaxException e )
			{
				myLog.severe( "Invalid broker specified" );
				res = false;
			}
		}
	} else
	{
		myLog.info( result.getParseResult() );
	}

	if( ! parser.getBool( "--log-to-console" ) )
	{
		myLog.removeHandler( console );
	}

	Message.setTopicRoot( myParser.getString( "--topic" ) );

	if( res )
	{
		// Let the module initialize
		res = initializeModule( myParser );
	}

	return res;
}

public boolean run( IModuleRunner stateProvider )
{

	myCom.start( stateProvider );

	try
	{
		while( ! myIsTerminated )
		{
			Thread.sleep( 0, 1 );
			myCom.tick();
		}
	}
	catch( InterruptedException e )
	{
		myLog.finest( e.getMessage() );
	}

	return myIsTerminated;
}

// Call this to terminate the module
public void terminate()
{
	myIsTerminated = true;
}

private Communicator myCom = null;
private final String myModuleName;
private IParseResult myResult;
private CmdParser4J myParser;
private XMLConfigurationReader myCfg;
protected Logger myLog;
private String myBroker;
protected Path myWorkDir;
private boolean myIsTerminated = false;
}
