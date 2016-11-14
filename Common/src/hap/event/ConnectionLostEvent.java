package hap.event;

public class ConnectionLostEvent extends EventBase {

	public ConnectionLostEvent(Throwable e) {
		myThrowable = e;
	}

	@Override
	public void visit(IEventListener listener) {
		listener.accept(this);
	}

	private final Throwable myThrowable;

	public Throwable getThrowable() {
		return myThrowable;
	}
}
