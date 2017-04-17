package hap.ruleengine.component.network.mqtt.helpers

import chainedfsm.EnterLeaveState
import org.eclipse.paho.client.mqttv3.IMqttToken

/**
 * Created by Per Malmberg on 2017-02-21.
 */
open class MqttState(val fsm: MqttConnection) : EnterLeaveState()
{
	protected var shouldDisconnect = false

	open fun connect()
	{
		fsm.setState(ConnectingState(fsm))
	}

	open fun connectionLost()
	{
	}

	open fun success(token: IMqttToken)
	{

	}

	open fun failure(token: IMqttToken)
	{
		if (shouldDisconnect)
		{
			fsm.setState(DisconnectState(fsm))
		}
		else
		{
			// Try again
			fsm.setState(ReconnectState(fsm))
		}
	}

	open fun disconnect()
	{
		shouldDisconnect = true
	}

	open fun publish(topic: String, msg: String)
	{

	}
}