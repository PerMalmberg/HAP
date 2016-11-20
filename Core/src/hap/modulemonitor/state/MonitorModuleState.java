package hap.modulemonitor.state;

import chainedfsm.EnterChain;
import hap.communication.Communicator;
import hap.communication.state.CommState;
import hap.event.MessageEvent;
import hap.event.timed.ITimedEventListener;
import hap.event.timed.PingTimeoutEvent;
import hap.event.timed.ResponseTimeoutEvent;
import hap.message.Message;
import hap.message.MessageFactory;
import hap.message.cmd.Ping;
import hap.message.cmd.Start;
import hap.message.cmd.Stop;
import hap.message.response.PingResponse;
import hap.message.response.StartResponse;
import hap.modulemonitor.ActiveModules;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class MonitorModuleState extends CommState implements ITimedEventListener
{


private final MessageFactory myMessageFactory = new MessageFactory();
private final ActiveModules myModules;
private final Logger myLog;
private final Path myModuleDir;
private final Path myWorkingDir;
private final Path myConfigDir;
private boolean myFirstStartCompleted = false;

public MonitorModuleState( Communicator com, ActiveModules activeMod, Path moduleDir, Path workingDir, Path configDir )
{
	super( com );
	myModules = activeMod;
	myLog = com.getLogger();
	myModuleDir = moduleDir;
	myWorkingDir = workingDir;
	myConfigDir = configDir;
	new EnterChain<>( this, this::enter );
}

private void enter()
{
	sendPing();
}

private void sendPing()
{
	// Send a ping to see which modules that are alive already and setup a timeout
	publish( new Ping() );
	myCom.startSingleShotTimer( Instant.now().plusSeconds( 3 ), new PingTimeoutEvent( this ) );
}

private void startAllModules()
{
	startModule( null );
}

private boolean startModule( String specificModule )
{
	boolean res = false;
	File wDir = myModuleDir.toFile();
	File[] jarFiles = wDir.listFiles( ( dir, name ) -> {
		return name.endsWith( ".jar" );
	} );

	for( int i = 0; ! res && i < jarFiles.length; ++ i )
	{
		File jar = jarFiles[i];

		try
		{
			ZipFile zip = new ZipFile( jar );
			ZipEntry manifest = zip.getEntry( "META-INF/MANIFEST.MF" );
			if( manifest == null )
			{
				myLog.warning( "No META-INF/MANIFEST.MF found in " + jar.getAbsolutePath() );
			} else
			{
				InputStream zin = zip.getInputStream( manifest );
				Manifest m = new Manifest( zin );
				Attributes attributes = m.getMainAttributes();
				String moduleName = attributes.getValue( "Main-Class" );

				if( moduleName == null )
				{
					myLog.warning( "No Main-Class in manifest of " + jar.getAbsolutePath() );
				} else if( specificModule == null || specificModule.equals( moduleName ) )
				{ // Loading all or a specific module
					if( ! myModules.isModuleActive( moduleName ) && myModules.mayStart( moduleName ) )
					{

						ProcessBuilder pb = new ProcessBuilder();

						pb.command( "java", "-Xms20m",
								"-jar",
								jar.getAbsolutePath(),
								"-w", myWorkingDir.toString(),
								"--broker", myCom.getClient().getServerURI(),
								"--topic", Message.getTopicRoot(),
								"--config", Paths.get( myConfigDir.toString(), moduleName + ".config" ).toString(),
								"-l" );

						pb.inheritIO();

						try
						{
							myLog.finest( "Starting module '" + moduleName + "'." );
							Process p = pb.start();
							myModules.update( moduleName, p );
							res = true;
						}
						catch( IOException e )
						{
							myLog.severe( e.getMessage() );
							myModules.delayStart( moduleName );
						}
					}
				}
			}

			zip.close();
		}
		catch( IOException e )
		{
			myLog.severe( "Error while loading module: " + e.getMessage() );
		}
	}

	return res;
}

@Override
public void tick()
{
	myModules.tick();
}

@Override
public void accept( MessageEvent e )
{
	Message m = myMessageFactory.Create( e.getTopic(), e.getMsg() );
	if( m != null )
	{
		m.visit( myModules );
	}
}

@Override
public void accept( Start s )
{
	s.visit( myModules );

	String name = new String( s.getPayload() );
	myCom.publish( new StartResponse( name, startModule( name ) ) );
}

@Override
public void accept( Stop s )
{
	s.visit( myModules );
}

@Override
public void accept( PingResponse msg )
{
	msg.visit( myModules );
}

@Override
public void accept( PingTimeoutEvent e )
{
	if( ! myFirstStartCompleted )
	{
		// Ping has timed out, check what modules are running and which ones that needs to be started.
		startAllModules();
		myFirstStartCompleted = true;
	}
	sendPing();
}

@Override
public void accept( ResponseTimeoutEvent responseTimeoutEvent )
{

}

}
