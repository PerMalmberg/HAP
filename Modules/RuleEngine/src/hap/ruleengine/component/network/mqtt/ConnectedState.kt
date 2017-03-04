package hap.ruleengine.component.network.mqtt

import chainedfsm.EnterChain
import chainedfsm.interfaces.IEnter
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttMessage

/**
 * Created by Per Malmberg on 2017-02-21.
 */
class ConnectedState(fsm: MqttConnection) : MqttState(fsm)
{
	private var token: IMqttToken? = null

	init
	{
		EnterChain(this, IEnter { enter() })
	}

	fun enter()
	{
		val topic = fsm.getTopic()
		if( !topic.trim().isEmpty())
		{
			token = fsm.getClient().subscribe(topic, 1, null, fsm, fsm)
		}
		System.out.println("Connected")
	}

	override fun connect()
	{
		fsm.setState(ReconnectState(fsm))
	}

	override fun success(token: IMqttToken)
	{
		if( this.token == token )
		{
			System.out.println("Subscribed")
		}
	}

	override fun publish(topic: String, msg: String)
	{
		val toSend = MqttMessage(msg.toByteArray())
		toSend.isRetained = false
		toSend.qos = 1

		fsm.getClient().publish(topic, toSend)
	}

	override fun disconnect()
	{
		fsm.setState(DisconnectState(fsm))
	}
}