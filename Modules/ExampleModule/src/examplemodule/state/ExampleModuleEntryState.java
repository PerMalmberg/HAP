// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package examplemodule.state;

import examplemodule.ExampleModule;
import hap.communication.Communicator;
import hap.communication.state.CommState;
import hap.communication.state.ConnectState;
import hap.event.FailureEvent;
import hap.message.cmd.Ping;
import hap.message.response.PingResponse;


public class ExampleModuleEntryState extends CommState {
	public ExampleModuleEntryState(Communicator com) {
		super(com);
	}

	@Override
	public void accept(Ping msg) {
		// All modules must always respond with a PingResponse to let the HAP Core know that the
		// module still is alive.
		publish(new PingResponse(ExampleModule.class.getName()));
	}

	@Override
	public void accept(FailureEvent event )
	{
		// Reconnect whenever a failure occurs
		myCom.setState(new ConnectState(myCom));
	}
}
