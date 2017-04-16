// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.state

import hap.ruleengine.RuleEngine
import java.util.logging.Logger


abstract class BaseState(val engine : RuleEngine) : chainedfsm.EnterLeaveState()
{
	protected var myLog: Logger = Logger.getLogger("RuleEngine")
	open fun tick() {}
}

