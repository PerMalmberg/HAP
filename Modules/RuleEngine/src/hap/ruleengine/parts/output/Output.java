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

public abstract class Output<T> extends ConnectionPoint implements IOutput
{
	private T myValue;
	private List<Input<T>> myRemote = new ArrayList<>();
	private int myCallCount = 0;

	Output( String name, IComponent parent, T defaultValue, boolean isVisibleWhenParentIsVisualized )
	{
		super( name, parent, isVisibleWhenParentIsVisualized );
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

	public boolean connect( Input<T> remote )
	{
		boolean res = ! remote.isConnected();
		if( res )
		{
			myRemote.add( remote );
			remote.markConnected();
		}
		return res;
	}

	public void disconnect( Input<T> remote )
	{
		myRemote.remove( remote );
		remote.markDisconnected();
	}

	@Override
	public void disconnectAll()
	{
		for( Input<T> input : myRemote ) {
			input.markDisconnected();
		}
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
