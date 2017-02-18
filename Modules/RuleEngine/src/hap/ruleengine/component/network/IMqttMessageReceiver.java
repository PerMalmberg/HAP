package hap.ruleengine.component.network;

import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by Per Malmberg on 2017-02-17.
 */
public interface IMqttMessageReceiver
{
	void messageArrived( String topic, MqttMessage msg);
}
