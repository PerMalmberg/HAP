// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.communication.state;

import hap.communication.Communicator;
import hap.event.*;
import hap.event.timed.PingTimeoutEvent;
import hap.message.IMessageListener;
import hap.message.Message;
import hap.message.cmd.Ping;
import hap.message.cmd.Start;
import hap.message.cmd.Stop;
import hap.message.general.UnclassifiedMessage;
import hap.message.response.PingResponse;
import hap.message.response.StartResponse;
import hap.message.response.StopResponse;

import java.util.logging.Logger;

public class CommState extends chainedfsm.EnterLeaveState implements IEventListener, IMessageListener
{

private final Logger myLog;
protected Communicator myCom;

public CommState( Communicator com )
{
	myCom = com;
	myLog = com.getLogger();
}

public void tick()
{

}

protected void publish( Message m )
{
	myCom.publish( m.getTopic(), m.getPayload(), m.getQos(), m.isRetained() );
}

@Override
public void accept( SuccessEvent e )
{

}

@Override
public void accept( FailureEvent e )
{
	myLog.warning( "Failure: " + e.toString() );
	myLog.warning( "Reconnecting" );
	myCom.setState( new ConnectState( myCom ) );
}

@Override
public void accept( MessageEvent e )
{

}

@Override
public void accept( PingTimeoutEvent e )
{

}

@Override
public void accept( ConnectionLostEvent e )
{
	myLog.warning( "Lost connection: " + e.getThrowable().getMessage() );
}

@Override
public void accept( Ping msg )
{

}

@Override
public void accept( Stop msg )
{

}

@Override
public void accept( Start msg )
{

}

@Override
public void accept( StopResponse stopResponse )
{

}

@Override
public void accept( StartResponse startResponse )
{

}

@Override
public void accept( PingResponse msg )
{

}

@Override
public void accept( UnclassifiedMessage msg )
{

}
}
