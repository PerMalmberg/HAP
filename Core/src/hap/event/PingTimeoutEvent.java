package hap.event;

public class PingTimeoutEvent extends EventBase {
	@Override
	public void visit(IEventListener listener) {
		listener.accept( this );
	}
}
