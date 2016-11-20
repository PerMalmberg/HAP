// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.message;

public class ControlTopic
{
public static final String CONTROL_TOPIC = "HAPControl";

public static String getControlTopic( String subTopic )
{
	return Message.combineTopic( Message.getTopicRoot(), CONTROL_TOPIC, subTopic );
}
}
