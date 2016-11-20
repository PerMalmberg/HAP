package hap.message;

import hap.message.general.UnclassifiedMessage;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.logging.Logger;

public class MessageFactory
{
private static Logger myLog = Logger.getLogger( "HAPCore" );
private HashMap<String, Constructor<?>> ctor = new HashMap<>();

public Message Create( String topic, MqttMessage msg )
{
	Message m = null;

	if( topic.contains( ControlTopic.CONTROL_TOPIC ) )
	{

		try
		{
			String[] part = topic.split( "/" );
			if( part.length > 0 )
			{
				Constructor<?> classToInstantiate = null;

				String possibleClassName = part[part.length - 1];
				if( ctor.containsKey( possibleClassName ) )
				{
					classToInstantiate = ctor.get( possibleClassName );
				} else
				{
					Class<?> clazz = findClass( possibleClassName, "hap.message.cmd.", "hap.message.response." );
					if( clazz != null )
					{
						classToInstantiate = clazz.getConstructor( String.class, byte[].class, Message.QOS.class, boolean.class );
						ctor.put( possibleClassName, classToInstantiate );
					}
				}

				if( classToInstantiate != null )
				{
					m = (Message) classToInstantiate.newInstance( topic, msg.getPayload(), Message.QOS.fromInt( msg.getQos() ), msg.isRetained() );
				}
			}
		}
		catch( NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | IllegalArgumentException ex )
		{
			myLog.finest( ex.getClass().getName() + ": " + ex.getMessage() );
		}
	} else
	{
		m = new UnclassifiedMessage( topic, msg.getPayload(), Message.QOS.fromInt( msg.getQos() ), msg.isRetained() );
	}

	return m;
}

private Class<?> findClass( String name, String... packageName )
{
	Class<?> clazz = null;

	for( int i = 0; clazz == null && i < packageName.length; ++ i )
	{
		try
		{
			clazz = Class.forName( packageName[i] + name );
		}
		catch( ClassNotFoundException ex )
		{
			myLog.finest( "No matching class: " + ex.getMessage() );
		}
	}

	return clazz;
}
}
