package hap.ruleengine.component.network.mqtt.helpers

import hap.ruleengine.component.network.mqtt.IMqttMessageReceiver
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import java.util.*

/**
 * Created by Per Malmberg on 2017-02-21.
 */
class MqttConnection(val receiver: IMqttMessageReceiver) : chainedfsm.FSM<MqttState>(), MqttCallback, IMqttActionListener, IMqttMessageListener
{
	private val client = MqttAsyncClient("tcp://ignored.tld", UUID.randomUUID().toString(), MemoryPersistence())

	init
	{
		setState(IdleState(this))
	}

	override fun messageArrived(topic: String?, message: MqttMessage?)
	{
		synchronized(client)
		{
			receiver.connectionStatus(client.isConnected)
			receiver.messageArrived(topic, message)
		}
	}

	override fun connectionLost(cause: Throwable?)
	{
		synchronized(client)
		{
			receiver.connectionStatus(client.isConnected)
			currentState.connectionLost()
		}
	}

	override fun deliveryComplete(token: IMqttDeliveryToken?)
	{
		synchronized(client)
		{
			receiver.connectionStatus(client.isConnected)
		}
	}

	override fun onSuccess(token: IMqttToken)
	{
		synchronized(client)
		{
			receiver.connectionStatus(client.isConnected)
			currentState.success(token)
		}
	}

	override fun onFailure(token: IMqttToken, exception: Throwable?)
	{
		synchronized(client)
		{
			receiver.connectionStatus(client.isConnected)
			currentState.failure(token)
		}
	}

	fun getClient() = client


	fun connect()
	{
		synchronized(client)
		{
			currentState.connect()
		}
	}

	fun disconnect()
	{
		synchronized(client)
		{
			currentState.disconnect()
		}
	}

	fun reconnect()
	{
		synchronized(client)
		{
			setState(ReconnectState(this))
		}
	}

	fun publish(topic: String, msg: String)
	{
		synchronized(client)
		{
			currentState.publish(topic, msg)
		}
	}

	fun getOptions(): MqttConnectOptions
	{
		val info = receiver.connectionInfo
		val opt = MqttConnectOptions()
		opt.isAutomaticReconnect = true
		opt.isCleanSession = true

		if (info.user.isNotEmpty() && info.password.isNotEmpty())
		{
			opt.userName = info.user
			opt.password = info.password.toCharArray()
		}

		var url: String
		if (info.secure)
		{
			url = "ssl://"
		}
		else
		{
			url = "tcp://"
		}
		url += info.broker
		if (info.port != 0)
		{
			url += ":" + info.port
		}

		opt.serverURIs = arrayOf(url)
		return opt
	}

	fun getTopic() = receiver.connectionInfo.topic

}