// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.modulemonitor;


import hap.SysUtil;
import hap.communication.Communicator;
import hap.communication.IModuleRunner;
import hap.communication.state.CommState;
import hap.message.Message;
import hap.modulemonitor.state.MonitorModuleState;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class ModuleMonitor implements IModuleRunner
{

private final Path myWorkingDir;
private final Path myModuleDir;
private final ActiveModules myActiveModules;
private Communicator myCom;
private boolean myIsTerminated = false;
private Path myConfigDir;
private Logger myLog = Logger.getLogger( "HAPCore" );
public ModuleMonitor( Path workingDir, Path moduleDir, String broker )
{
	myWorkingDir = workingDir;
	myModuleDir = moduleDir;
	myCom = new Communicator( broker, "HAPCore-" + Message.getTopicRoot(), myLog );
	myActiveModules = new ActiveModules( myCom );
}

public boolean tick()
{
	myCom.tick();
	return ! myIsTerminated;
}

public boolean start()
{
	myLog.info( "Starting Module Monitor" );

	myConfigDir = Paths.get( SysUtil.getDirectoryOfJar( MonitorModuleState.class ), "config" );
	myLog.finest( "Configuration directory: " + myConfigDir );

	Runtime.getRuntime().addShutdownHook(new Thread(myActiveModules::killAll));

	return myCom.start( this );
}

@Override
public CommState createEntryState( Communicator com )
{
	return new MonitorModuleState( com, myActiveModules, myModuleDir, myWorkingDir, myConfigDir );
}

@Override
public void terminate()
{
	myIsTerminated = true;
}

}
