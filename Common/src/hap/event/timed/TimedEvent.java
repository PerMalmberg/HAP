package hap.event.timed;

import java.time.Instant;

public class TimedEvent
{
private final Instant myInstant;
private final TimedEventBase myEvent;


public TimedEvent( Instant instant, TimedEventBase event )
{
	myInstant = instant;
	myEvent = event;
}

public Instant getInstant()
{
	return myInstant;
}

public TimedEventBase getEvent()
{
	return myEvent;
}
}
