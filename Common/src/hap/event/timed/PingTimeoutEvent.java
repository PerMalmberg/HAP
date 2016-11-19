package hap.event.timed;

public class PingTimeoutEvent extends TimedEventBase {

	public PingTimeoutEvent(ITimedEventListener target) {
		super(target);
	}

	@Override
	protected void visit(ITimedEventListener listener) {
		listener.accept(this);
	}

}
