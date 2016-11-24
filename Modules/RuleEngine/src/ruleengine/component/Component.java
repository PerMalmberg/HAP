// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package ruleengine.component;

import ruleengine.xpath.IXPathReader;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public abstract class Component implements IComponent
{
	private final UUID myInstanceId;
	private final HashMap<UUID, IComponent> mySubComponent = new HashMap<>();


	public Component()
	{
		myInstanceId = UUID.randomUUID();
	}

	public Component( UUID id )
	{
		myInstanceId = id;
	}

	@Override
	public boolean loadComponentFromData( String componentData, IComponentFactory factory, IXPathReader reader )
	{
		List<String> imports = getImports( componentData, reader );

		boolean res = true;

		for( int i = 0; res && i < imports.size(); ++ i )
		{
			String file = imports.get( i );
			IComponent c = factory.create( new File( file ) );

			if( c == null )
			{
				res = false;
			}
			else
			{
				mySubComponent.put( c.getId(), c );
			}
		}

		return res;
	}

	public List<String> getImports( String componentData, IXPathReader reader )
	{
		return reader.getTextValues( "/Component/Imports/*", componentData );
	}

	@Override
	public HashMap<String, IOutput> getOutputs()
	{
		return null;
	}

	@Override
	public HashMap<String, IInput> getInputs()
	{
		return null;
	}

	@Override
	public UUID getId()
	{
		return myInstanceId;
	}


	@Override
	public List<IWire> getWires()
	{
		return null;
	}
}
