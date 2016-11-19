package hap.message;


import hap.message.cmd.Ping;
import hap.message.cmd.Start;
import hap.message.cmd.Stop;
import hap.message.general.UnclassifiedMessage;
import hap.message.response.PingResponse;
import hap.message.response.StartResponse;
import hap.message.response.StopResponse;

public interface IMessageListener {
	void accept(Ping msg);

	void accept(PingResponse msg);

	void accept(UnclassifiedMessage msg);

	void accept(Stop msg);

	void accept(Start start);

	void accept(StopResponse stopResponse);

	void accept(StartResponse startResponse);

}
