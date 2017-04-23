package hap.ruleengine.component.network.mqtt.helpers

import hap.ruleengine.component.network.mqtt.IMqttMessageReceiver
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import java.util.*
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.logging.Logger

/**
 * Created by Per Malmberg on 2017-02-21.
 */
class MqttConnection(val receiver: IMqttMessageReceiver) : MqttCallback, Runnable
{
	var run = true
	val log: Logger = Logger.getLogger("MqttConnection")

	private val client = MqttClient("tcp://ignored.tld", UUID.randomUUID().toString(), MemoryPersistence())
	private val outgoing = ConcurrentLinkedDeque<Pair<String, MqttMessage>>()
	private var thread = Thread(this)

	init
	{
		client.setCallback(this)
	}

	override fun run()
	{
		while (run)
		{
			receiver.connectionStatus(client.isConnected)

			if (client.isConnected)
			{
				while (outgoing.size > 0)
				{
					val out = outgoing.pop()

					try
					{
						client.publish(out.first, out.second)
					}
					catch (e: Exception)
					{
						log.severe(e.message)
					}
				}
			}
			else
			{
				try
				{
					// Wait some between reconnecting
					Thread.sleep(500)
					client.connect(getOptions())
					log.finest("Client with UUID ${client.clientId} connected")

					if (!getTopic().isEmpty())
					{
						client.subscribe(getTopic())
						log.finest("Client with UUID ${client.clientId} subscribed to ${getTopic()}")
					}
				}
				catch (e: Exception)
				{
					log.severe(e.message)
				}
			}
		}

		try
		{
			if (client.isConnected)
			{
				client.disconnect()
			}
		}
		catch (e: Exception)
		{
			log.severe(e.message)
		}

		receiver.connectionStatus(client.isConnected)
	}

	fun connect()
	{
		thread = Thread(this)
		run = true
		thread.start()
	}

	fun disconnect()
	{
		if (thread.isAlive)
		{
			run = false
			thread.join()
			receiver.connectionStatus(false)
			log.finest("Client with UUID ${client.clientId} disconnected")
		}
	}

	fun reconnect()
	{
		disconnect()
		connect()
	}

	fun publish(topic: String, msg: String)
	{
		val m = MqttMessage(msg.toByteArray())
		m.isRetained = false
		m.qos = 2

		try
		{
			client.publish(topic, m)
		}
		catch (e: Exception)
		{
			log.severe(e.message)
		}
	}

	override fun messageArrived(topic: String?, msg: MqttMessage?)
	{
		if (topic != null && msg != null)
		{
			receiver.messageArrived(topic, msg)
		}
	}

	override fun connectionLost(token: Throwable?)
	{

	}

	override fun deliveryComplete(token: IMqttDeliveryToken?)
	{

	}

	fun getOptions(): MqttConnectOptions
	{
		val info = receiver.connectionInfo
		val opt = MqttConnectOptions()
		opt.isAutomaticReconnect = false
		opt.isCleanSession = true
		opt.connectionTimeout = 3000

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