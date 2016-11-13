package hap.message;

import hap.message.cmd.Ping;
import hap.message.response.PingResponse;

// This class is used to hide methods in the IMessageListener
// when the inheriting class ony implements/overrides a few of the methods.
public abstract class MessageListener implements IMessageListener {
	@Override
	public void accept(Ping msg) {

	}

	@Override
	public void accept(PingResponse msg) {

	}
}
