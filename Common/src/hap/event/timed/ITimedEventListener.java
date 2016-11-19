package hap.event.timed;


public interface ITimedEventListener {
	void accept(PingTimeoutEvent pingTimeoutEvent);

	void accept(ResponseTimeoutEvent responseTimeoutEvent);
}
