package hap.event.timed;

import java.time.Instant;

public class TimedEvent {
	public Instant getInstant() {
		return myInstant;
	}

	public TimedEventBase getEvent() {
		return myEvent;
	}


	public TimedEvent(Instant instant, TimedEventBase event) {
		myInstant = instant;
		myEvent = event;
	}

	private final Instant myInstant;
	private final TimedEventBase myEvent;
}
