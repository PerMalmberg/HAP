// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap;

import chainedfsm.EnterChain;
import hap.basemodule.ModuleRunner;
import hap.communication.Communicator;
import hap.communication.state.CommState;
import hap.event.timed.ITimedEventListener;
import hap.event.timed.ResponseTimeoutEvent;
import hap.message.Message;
import hap.message.response.StartResponse;
import hap.message.response.StopResponse;

import java.time.Instant;
import java.util.ArrayList;


public class ExecuteCommandState extends CommState implements ITimedEventListener
{

ExecuteCommandState( Communicator com, ArrayList<Message> commands, ModuleRunner runner )
{
	super( com );
	myCommands = commands;
	myRunner = runner;

	new EnterChain<>( this, this::enter );
}

private void enter()
{
	for( Message m : myCommands )
	{
		myCom.publish( m );
	}
	myCommands.clear();

	myCom.startSingleShotTimer( Instant.now().plusSeconds( 3 ), new ResponseTimeoutEvent( this ) );
}

@Override
public void accept( StartResponse stop )
{
	myCom.getLogger().info( "Start result: " + new String( stop.getPayload() ) );
	myRunner.terminate();
}

@Override
public void accept( StopResponse stop )
{
	myCom.getLogger().info( "Stop result: " + new String( stop.getPayload() ) );
	myRunner.terminate();
}

private final ArrayList<Message> myCommands;
private final ModuleRunner myRunner;

@Override
public void accept( ResponseTimeoutEvent responseTimeoutEvent )
{
	myCom.getLogger().warning( "No response to command" );
	myRunner.terminate();
}
}
