// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.state

import hap.ruleengine.RuleEngine


abstract class BaseState(val engine : RuleEngine) : chainedfsm.EnterLeaveState()
{
	open fun tick() {}
}

