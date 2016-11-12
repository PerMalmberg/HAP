package hap.event;

/**
 * Created by Per Malmberg on 2016-11-12.
 */
public class PingTimeoutEvent extends EventBase {
	@Override
	public void visit(IEventListener listener) {
		listener.accept( this );
	}
}
