package hap.message;


import hap.message.cmd.Ping;
import hap.message.response.PingResponse;

public interface IMessageListener {
	void accept( Ping msg );
	void accept( PingResponse msg );
}
