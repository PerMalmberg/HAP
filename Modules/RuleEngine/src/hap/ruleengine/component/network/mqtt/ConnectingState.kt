package hap.ruleengine.component.network.mqtt

import chainedfsm.EnterChain
import chainedfsm.interfaces.IEnter
import org.eclipse.paho.client.mqttv3.IMqttToken

/**
 * Created by Per Malmberg on 2017-02-21.
 */
class ConnectingState(fsm: MqttConnection) : MqttState(fsm)
{
	init
	{
		EnterChain(this, IEnter { enter() })
	}

	fun enter()
	{
		fsm.getClient().connect(fsm.getOptions(), null, fsm)
	}

	override fun failure(token: IMqttToken)
	{
		if (shouldDisconnect)
		{
			fsm.setState(DisconnectState(fsm))
		}
		else
		{
			// Try again
			fsm.setState(ConnectingState(fsm))
		}
	}

	override fun success(token: IMqttToken)
	{
		if (shouldDisconnect)
		{
			fsm.setState(DisconnectState(fsm))
		}
		else
		{
			fsm.setState(ConnectedState(fsm))
		}
	}
}


