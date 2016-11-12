package hap.state;

import hap.event.EventBase;

import java.time.Instant;
import java.util.Comparator;

public class TimedEvent {
	public Instant getInstant() {
		return myInstant;
	}

	public EventBase getEvent() {
		return myEvent;
	}



	public TimedEvent( Instant instant, EventBase event )
	{
		myInstant = instant;
		myEvent = event;
	}

	private final Instant myInstant;
	private final EventBase myEvent;
}
