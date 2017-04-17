package hap.ruleengine.component.network.mqtt

/**
 * Created by Per Malmberg on 2017-02-21.
 */
data class ConnectionInfo(val broker: String, val user: String, val password: String, val port: Int, val secure: Boolean, val topic: String)