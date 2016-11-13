package hap.message;


import hap.message.cmd.Ping;
import hap.message.general.UnclassifiedMessage;
import hap.message.response.PingResponse;

public interface IMessageListener {
	void accept(Ping msg);

	void accept(PingResponse msg);

	void accept(UnclassifiedMessage msg);
}
