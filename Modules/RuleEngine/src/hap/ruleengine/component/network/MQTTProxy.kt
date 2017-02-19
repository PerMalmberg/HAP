package hap.ruleengine.component.network

import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import java.util.*

/**
 * Created by Per Malmberg on 2017-02-16.
 */
class MQTTProxy(val server: String) : MqttCallback, IMqttActionListener
{
	private val QOS = 1
	private val opt = MqttConnectOptions()
	private var client: MqttAsyncClient? = null
	private val subscriptions = HashMap<String, ArrayList<IMqttMessageReceiver>>()

	private var resubscribe = true
	private var connectToken: IMqttToken? = null

	fun start(user: String, password: String, port: Int, secure: Boolean)
	{
		if (client == null)
		{
			opt.isAutomaticReconnect = false
			opt.isCleanSession = false

			if (user.isNotEmpty() && password.isNotEmpty())
			{
				opt.userName = user
				opt.password = password.toCharArray()
			}

			var url: String
			if (secure)
			{
				url = "ssl://"
			}
			else
			{
				url = "tcp://"
			}
			url += server
			if (port != 0)
			{
				url += ":" + port
			}

			client = MqttAsyncClient(url, UUID.randomUUID().toString(), MemoryPersistence())
			client?.setCallback(this)
			connectToken = client!!.connect(opt, this)
		}
	}

	fun stop()
	{
		client?.disconnect()
		client = null
	}

	override fun onSuccess(token: IMqttToken?)
	{
		if (connectToken == token)
		{
			if (resubscribe)
			{
				synchronized(subscriptions) {
					subscriptions.map {
						client?.subscribe(it.key, QOS)
					}
				}
			}

			resubscribe = false
		}
	}

	override fun onFailure(token: IMqttToken?, ex: Throwable?)
	{

	}

	override fun connectionLost(ex: Throwable)
	{
		resubscribe = true
		connectToken = client?.connect(opt, this)
	}

	override fun deliveryComplete(token: IMqttDeliveryToken?)
	{

	}

	override fun messageArrived(topic: String?, msg: MqttMessage?)
	{
		synchronized(subscriptions) {
			subscriptions[topic]?.map { it.messageArrived(topic, msg) }
		}
	}

	fun subscribe(topic: String, receiver: IMqttMessageReceiver)
	{
		synchronized(subscriptions) {
			val receivers = subscriptions.getOrPut(topic, { ArrayList<IMqttMessageReceiver>() })
			receivers.add(receiver)
		}

		if (true == client?.isConnected)
		{
			client?.subscribe(topic, QOS)
		}
	}

	fun unsubscribe(receiver: IMqttMessageReceiver)
	{
		synchronized(subscriptions)
		{
			// Remove receivers
			subscriptions.toList().map {
				if (it.second.contains(receiver))
				{
					it.second.remove(receiver)
				}
			}

			// Remove element if no receivers are left
			subscriptions.toList().map {
				if (it.second.isEmpty())
				{
					client?.unsubscribe(it.first)
					subscriptions.remove(it.first)
				}
			}

			if (subscriptions.isEmpty())
			{
				client?.disconnect()
				client = null
			}

		}
	}

	fun publish(topic: String, msg: String)
	{
		client?.publish(topic, MqttMessage(msg.toByteArray()))
	}

}