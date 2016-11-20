package hap.event.timed;


public abstract class TimedEventBase
{
protected ITimedEventListener myTarget;

public TimedEventBase( ITimedEventListener target )
{
	myTarget = target;
}

public void execute()
{
	visit( myTarget );
}

protected abstract void visit( ITimedEventListener listener );
}
