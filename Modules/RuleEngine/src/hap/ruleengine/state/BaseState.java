// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.state;

import hap.communication.Communicator;
import hap.communication.state.CommState;
import hap.message.cmd.Ping;
import hap.message.response.PingResponse;
import hap.ruleengine.RuleEngine;

public abstract class BaseState extends CommState
{
	public BaseState( Communicator com )
	{
		super( com );
	}

	@Override
	public void accept( Ping msg )
	{
		publish( new PingResponse( RuleEngine.class.getName() ) );
	}
}

