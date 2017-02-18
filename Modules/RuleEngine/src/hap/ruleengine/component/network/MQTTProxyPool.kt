package hap.ruleengine.component.network

import java.util.concurrent.ConcurrentHashMap

/**
 * Created by Per Malmberg on 2017-02-16.
 */
class MQTTProxyPool
{
	companion object Factory
	{
		@JvmStatic
		private val pool = ConcurrentHashMap<String, MQTTProxy>()

		fun getProxy(server: String): MQTTProxy
		{
			return pool.getOrPut(server, { MQTTProxy(server) })
		}
	}
}