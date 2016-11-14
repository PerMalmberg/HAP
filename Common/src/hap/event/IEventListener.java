package hap.event;


public interface IEventListener {
	void accept( SuccessEvent e );
	void accept( FailureEvent e );
	void accept( MessageEvent e );
	void accept( PingTimeoutEvent e );
	void accept(ConnectionLostEvent e );
}
