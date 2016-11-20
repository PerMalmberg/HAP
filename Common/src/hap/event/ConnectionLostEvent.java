package hap.event;

public class ConnectionLostEvent extends EventBase
{

private final Throwable myThrowable;

public ConnectionLostEvent( Throwable e )
{
	myThrowable = e;
}

@Override
public void visit( IEventListener listener )
{
	listener.accept( this );
}

public Throwable getThrowable()
{
	return myThrowable;
}
}
