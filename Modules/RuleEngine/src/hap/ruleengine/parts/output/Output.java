// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.parts.output;

import hap.ruleengine.parts.ConnectionPoint;
import hap.ruleengine.parts.IComponent;
import hap.ruleengine.parts.data.CompositeDef;
import hap.ruleengine.parts.data.WireDef;
import hap.ruleengine.parts.input.Input;

import java.util.ArrayList;
import java.util.List;

public class Output<T> extends ConnectionPoint implements IOutput
{
	private T myValue;
	private List<Input<T>> myRemote = new ArrayList<>();
	private IComponent myParent;
	private int myCallCount = 0;

	Output( String name, IComponent parent, T defaultValue, boolean isVisibleOnComponent )
	{
		super(name, isVisibleOnComponent);
		myParent = parent;
		myValue = defaultValue;
	}

	public void set( T value )
	{
		// Prevent recursive call-chains
		if( myCallCount == 0 )
		{
			++ myCallCount;
			myValue = value;
			myRemote.forEach( o -> o.set( value ) );
			-- myCallCount;
		}
		else
		{
			// TODO: Log event
		}
	}

	public T getValue()
	{
		return myValue;
	}

	public void connect( Input<T> remote )
	{
		myRemote.add( remote );
	}

	public void disconnect( Input<T> remote )
	{
		myRemote.remove( remote );
	}

	@Override
	public void disconnectAll()
	{
		myRemote.clear();
	}

	@Override
	public void store( CompositeDef data )
	{
		for( Input<T> remote : myRemote )
		{
			WireDef wire = new WireDef();
			wire.setSourceComponent( myParent.getId().toString() );
			wire.setSourceOutput( getName() );
			wire.setTargetComponent( remote.getParent().getId().toString() );
			wire.setTargetInput( remote.getName() );
			wire.setType( this.getClass().getSimpleName().replace( "Output", "Wire" ) );
			data.getWires().getWireDef().add( wire );
		}
	}
}
