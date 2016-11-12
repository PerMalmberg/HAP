package hap.event;


public abstract class EventBase {
	public abstract void visit(IEventListener listener);
}