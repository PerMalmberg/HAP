package hap.communication.state;

import chainedfsm.EnterChain;
import hap.communication.Communicator;
import hap.event.SuccessEvent;
import hap.message.ControlTopic;
import hap.message.Message;

import java.util.logging.Logger;

public class SubscribeState extends CommState
{

private final String ctrlTopic = ControlTopic.getControlTopic( "/#" );
private Logger myLog;

public SubscribeState( Communicator com )
{
	super( com );
	myLog = com.getLogger();
	new EnterChain<>( this, this::subscribe );
}

private void subscribe()
{
	myCom.subscribe( ctrlTopic, Message.QOS.AtMostOnce );
}

@Override
public void accept( SuccessEvent e )
{
	// Pass control to the applications entry state
	myLog.finest( "Subscribed to control topic: " + ctrlTopic );
	myCom.setState( myCom.getStateProvider().createEntryState( myCom ) );
}
}
