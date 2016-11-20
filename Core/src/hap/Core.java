// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap;

import cmdparser4j.CmdParser4J;
import cmdparser4j.IParseResult;
import cmdparser4j.SystemOutputParseResult;
import cmdparser4j.SystemOutputUsageFormatter;
import hap.message.Message;
import hap.modulemonitor.ModuleMonitor;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.*;

public class Core
{

private final Logger myLog = Logger.getLogger( "HAPCore" );
private String[] myArgs;
private IParseResult myResult;
private CmdParser4J myParser;
private Path myModDir;
private Path myWorkDir;
private String myBroker;
///////////////////////////////////////////////////////////////////////////
//
//
//
///////////////////////////////////////////////////////////////////////////
public Core( String[] args )
{
	myArgs = args;
	myResult = new SystemOutputParseResult();
	myParser = new CmdParser4J( myResult );
	myParser.accept( "--broker" ).asString( 1 ).describedAs( "The DNS or IP of the MQTT broker" ).setMandatory().withAlias( "-b" );
	myParser.accept( "--topic" ).asString( 1 ).describedAs( "The root topic for this instance" ).setMandatory().withAlias( "-r" );
	myParser.accept( "--config" ).asString( 1 ).describedAs( "Full path to the configuration" ).setMandatory().withAlias( "-c" );
	myParser.accept( "--working-dir" ).asString( 1 ).describedAs( "The working directory" ).withAlias( "-w" );
	myParser.accept( "--module-dir" ).asString( 1 ).describedAs( "Directory containing modules" ).withAlias( "-m" );
	myParser.accept( "--log-to-console" ).asSingleBoolean().describedAs( "If specified, logging to console will be enabled" ).withAlias( "-l" );
	myParser.accept( "--help" ).asSingleBoolean().describedAs( "Print help text" ).withAlias( "-?" );
}

///////////////////////////////////////////////////////////////////////////
//
//
//
///////////////////////////////////////////////////////////////////////////
public static void main( String[] args )
{
	Core c = new Core( args );
	int result = c.run();
	System.exit( result );
}

///////////////////////////////////////////////////////////////////////////
//
//
//
///////////////////////////////////////////////////////////////////////////
private int run()
{
	boolean res = setup();

	if( res )
	{

		Message.setTopicRoot( myParser.getString( "--topic" ) );
		ModuleMonitor mm = new ModuleMonitor( myWorkDir, myModDir, myBroker );

		try
		{
			if( mm.start() )
			{
				while( mm.tick() )
				{
					Thread.sleep( 0, 1 );
				}
			}
		}
		catch( InterruptedException e )
		{
			myLog.finest( e.getMessage() );
		}
	}

	return res ? 0 : 1;
}

private boolean setup()
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

	boolean res = myParser.parse( myArgs );

	if( res )
	{
		if( myParser.getBool( "--help" ) )
		{
			SystemOutputUsageFormatter usage = new SystemOutputUsageFormatter( "HAP::Core" );
			myParser.getUsage( usage );
			myLog.info( usage.toString() );
		} else
		{
			myWorkDir = Paths.get( myParser.getString( "--working-dir", 0, Paths.get( SysUtil.getDirectoryOfJar( Core.class ), "data" ).toAbsolutePath().toString() ) );
			myModDir = Paths.get( myParser.getString( "--module-dir", 0, Paths.get( SysUtil.getDirectoryOfJar( Core.class ), "modules" ).toAbsolutePath().toString() ) );
			myBroker = myParser.getString( "--broker" );

			if( Files.exists( myWorkDir ) )
			{
				try
				{
					FileHandler fh = new FileHandler( Paths.get( myWorkDir.toString(), "HAPCore.log" ).toString(), 1024 * 1024, 10, true );
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

			if( ! Files.exists( myModDir ) )
			{
				myLog.severe( "Module directory does not exist (" + myModDir.toString() + ")" );
				res = false;
			}

			try
			{
				URI uri = new URI( myParser.getString( "--broker" ) );
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
		myLog.info( myResult.getParseResult() );
	}

	if( ! myParser.getBool( "--log-to-console" ) )
	{
		myLog.removeHandler( console );
	}

	return res;
}
}
