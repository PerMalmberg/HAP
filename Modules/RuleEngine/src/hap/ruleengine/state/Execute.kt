package hap.ruleengine.state

import chainedfsm.EnterChain
import chainedfsm.LeaveChain
import chainedfsm.interfaces.IEnter
import chainedfsm.interfaces.ILeave
import hap.communication.Communicator
import hap.ruleengine.RuleEngine
import hap.ruleengine.parts.composite.CompositeComponent
import java.util.*

/**
 * Created by Per Malmberg on 2017-02-15.
 */
class Execute( val loadedSchema: HashMap<UUID, CompositeComponent>, engine: RuleEngine) : BaseState(engine), IEnter, ILeave
{
	init
	{
		EnterChain(this, this)
		LeaveChain(this, this)
	}

	override fun tick()
	{
		loadedSchema.forEach { _, compositeComponent ->
			compositeComponent.tick()
		}
	}

	override fun enter()
	{
		// Make components go live.
		loadedSchema.forEach { _, u -> u.executionState = true }
	}

	override fun leave()
	{
		loadedSchema.forEach { _, u -> u.executionState = false }
	}
}