// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.message.general;

import hap.message.IMessageListener;
import hap.message.Message;

public class UnclassifiedMessage extends Message
{
public UnclassifiedMessage( String topic, byte[] payload, QOS qos, boolean retained )
{
	super( topic, payload, qos, retained );
}

@Override
public void visit( IMessageListener listener )
{
	listener.accept( this );
}
}
