// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.basemodule;

import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;

public class BaseModule {

	private boolean myShallTerminate = false;
	private IMqttAsyncClient myClient;
}
