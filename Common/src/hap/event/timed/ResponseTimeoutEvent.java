package hap.event.timed;

public class ResponseTimeoutEvent extends TimedEventBase
{
public ResponseTimeoutEvent( ITimedEventListener target )
{
	super( target );
}

@Override
protected void visit( ITimedEventListener listener )
{
	listener.accept( this );
}
}
