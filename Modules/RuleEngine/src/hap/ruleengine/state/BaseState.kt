// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.state

import hap.communication.Communicator
import hap.communication.state.CommState
import hap.message.cmd.Ping
import hap.message.response.PingResponse
import hap.ruleengine.RuleEngine

abstract class BaseState(com: Communicator) : CommState(com)
{

	override fun accept(msg: Ping)
	{
		publish(PingResponse(RuleEngine::class.java.name))
	}
}

