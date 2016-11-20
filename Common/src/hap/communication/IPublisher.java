// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.communication;

import hap.message.Message;

public interface IPublisher
{
void publish( String topic, byte[] payload, Message.QOS qos, boolean retained );

void publish( Message m );
}
