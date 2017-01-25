// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.onewire.state;

import hap.communication.Communicator;
import hap.communication.state.CommState;
import hap.message.cmd.Ping;
import hap.message.response.PingResponse;
import hap.onewire.OneWire;


public class OwBaseState extends CommState
{
	protected final OneWire myOw;

	public OwBaseState( Communicator com, OneWire oneWire )
	{
		super(com);
		myOw = oneWire;
	}

	@Override
	public void accept( Ping msg )
	{
		publish(new PingResponse(OneWire.class.getName()));
	}
}
