package hap.message;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.logging.Logger;

public class MessageFactory {
	public Message Create(String topic, MqttMessage msg)
	{
		Message m = null;



		try {
			String[] part = topic.split("/");
			if( part.length > 0) {
				Constructor<?> classToInstanciate = null;

				String possibleClassName = part[part.length-1];
				if( ctor.containsKey(possibleClassName) ) {
					classToInstanciate = ctor.get(possibleClassName);
				}
				else {
					Class<?> clazz = findClass( possibleClassName, "hap.message.cmd.", "hap.message.response." );
					if( clazz != null ) {
						classToInstanciate = clazz.getConstructor(String.class, byte[].class,Message.QOS.class, boolean.class );
						ctor.put( possibleClassName, classToInstanciate );
					}
				}

				if( classToInstanciate != null ) {
					m = (Message) classToInstanciate.newInstance(topic, msg.getPayload(), Message.QOS.fromInt( msg.getQos() ), msg.isRetained());
				}
			}
		}
		catch( NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | IllegalArgumentException ex)
		{
			myLog.finest(ex.getClass().getName() + ": " + ex.getMessage());
		}

		return m;
	}

	private Class<?> findClass( String name, String... packageName ) {
		Class<?> clazz = null;

		for( int i = 0; clazz == null && i < packageName.length; ++i )
		{
			try {
				clazz = Class.forName( packageName[i] + name );
			}
			catch (ClassNotFoundException ex )
			{
				myLog.finest(ex.getClass().getName() + ": " + ex.getMessage());
			}
		}

		return clazz;
	}

	private HashMap<String, Constructor<?>> ctor = new HashMap<>();
	private static Logger myLog = Logger.getLogger("HAPCore");
}
