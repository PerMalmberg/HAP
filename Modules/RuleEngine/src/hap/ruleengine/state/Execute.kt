package hap.ruleengine.state

import chainedfsm.EnterChain
import chainedfsm.LeaveChain
import chainedfsm.interfaces.IEnter
import chainedfsm.interfaces.ILeave
import hap.communication.Communicator
import hap.ruleengine.parts.composite.CompositeComponent
import java.util.*

/**
 * Created by Per Malmberg on 2017-02-15.
 */
class Execute( val loadedSchema: HashMap<UUID, CompositeComponent>, com: Communicator) : BaseState(com), IEnter, ILeave
{
	init
	{
		EnterChain(this, this)
		LeaveChain(this, this)
	}

	override fun tick()
	{
		loadedSchema.forEach { uuid, compositeComponent ->
			compositeComponent.tick()
		}
	}

	override fun enter()
	{
		loadedSchema.map { it.value.executionState = true }
	}

	override fun leave()
	{

	}
}