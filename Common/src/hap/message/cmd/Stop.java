// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.message.cmd;

import hap.message.ControlTopic;
import hap.message.IMessageListener;
import hap.message.Message;

public class Stop extends Message {
	public Stop(String moduleName) {
		this(ControlTopic.getControlTopic(Stop.class.getSimpleName()), moduleName.getBytes(), QOS.AtMostOnce, false);
	}

	public Stop(String topic, byte[] payload, QOS qos, boolean retained) {
		super(topic, payload, qos, retained);
	}

	@Override
	public void visit(IMessageListener listener) {
		listener.accept(this);
	}
}
