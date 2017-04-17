package hap.ruleengine.component.network.mqtt.helpers

import chainedfsm.EnterChain
import chainedfsm.interfaces.IEnter
import org.eclipse.paho.client.mqttv3.IMqttToken

/**
 * Created by Per Malmberg on 2017-02-21.
 */
class DisconnectState(fsm: MqttConnection) : MqttState(fsm)
{
	private var token: IMqttToken? = null

	init
	{
		EnterChain(this, IEnter { enter() })
	}

	fun enter()
	{
		if (fsm.getClient().isConnected)
		{
			token = fsm.getClient().disconnect(null, fsm)
		}
		else
		{
			fsm.setState(IdleState(fsm))
		}
	}

	override fun failure(token: IMqttToken)
	{
		try
		{
			fsm.getClient().disconnectForcibly(0, 0)
		}
		catch (ex: Exception)
		{

		}
		fsm.setState(IdleState(fsm))
	}

	override fun success(token: IMqttToken)
	{
		if (token == this.token)
		{
			System.out.println("Disconnected")
			fsm.setState(IdleState(fsm))
		}
	}
}