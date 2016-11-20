package hap.message;


import java.util.Arrays;

public abstract class Message
{

private static String myTopicRoot = "";
final private String myTopic;
final private byte[] myPayload;
private QOS myQos;
private boolean myRetained;

public Message( String topic, byte[] payload, QOS qos, boolean retained )
{
	if( topic.startsWith( myTopicRoot ) )
	{
		myTopic = topic;
	} else
	{
		myTopic = combineTopic( myTopicRoot, topic );
	}
	myPayload = payload;
	myQos = qos;
	myRetained = retained;
}

public static String combineTopic( final String topicRoot, final String... topic )
{
	String t = topicRoot;

	for( String curr : topic )
	{
		if( ! curr.startsWith( "/" ) )
		{
			curr = "/" + curr;
		}

		if( curr.endsWith( "/" ) )
		{
			curr = curr.substring( 0, curr.length() - 1 );
		}

		t += curr;
	}

	return t;
}

public static String getTopicRoot()
{
	return myTopicRoot;
}

public static void setTopicRoot( String root )
{
	myTopicRoot = root;
}

public abstract void visit( IMessageListener listener );

public byte[] getPayload()
{
	return myPayload;
}

public String getTopic()
{
	return myTopic;
}

public QOS getQos()
{
	return myQos;
}

public boolean isRetained()
{
	return myRetained;
}

@Override
public String toString()
{
	String s = getTopic();

	if( isText( getPayload() ) )
	{
		s += " '" + new String( getPayload() ) + "'";
	} else
	{
		s += " " + Arrays.toString( getPayload() );
	}
	return s;
}

private boolean isText( byte[] payload )
{
	double hit = 0;

	for( byte b : payload )
	{
		if( b >= ' ' && b <= 126 )
		{
			++ hit;
		}
	}

	return hit / payload.length > 0.95;
}

public enum QOS
{
	AtMostOnce( 0 ),
	AtLeastOnce( 1 ),
	ExactlyOnce( 2 );

	private final int myValue;

	QOS( int value )
	{
		myValue = value;
	}

	public static QOS fromInt( int val )
	{
		if( val == 0 )
		{
			return AtMostOnce;
		} else if( val == 1 )
		{
			return AtLeastOnce;
		} else
		{
			return ExactlyOnce;
		}
	}

	public int getValue()
	{
		return myValue;
	}
}
}
