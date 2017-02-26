// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.parts.output;

import hap.ruleengine.parts.ConnectionPoint;
import hap.ruleengine.parts.IComponent;
import hap.ruleengine.parts.IValueChangeReceiver;
import hap.ruleengine.parts.data.CompositeDef;
import hap.ruleengine.parts.data.WireDef;
import hap.ruleengine.parts.input.Input;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Output<T> extends ConnectionPoint implements IOutput
{
	private T myValue;
	private List<Input<T>> myRemote = new ArrayList<>();
	private int myCallCount = 0;

	Output( String name, UUID id, IComponent parent, T defaultValue, boolean isVisibleWhenParentIsVisualized )
	{
		super( name, id, parent, isVisibleWhenParentIsVisualized );
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
			notifyValueSubscribers();
			-- myCallCount;
		}
		else
		{
			// TODO: Log event
		}
	}

	@Override
	public void notifyValueSubscribers()
	{
		for( IValueChangeReceiver rec : valueChangeReceivers )
		{
			rec.newValue( getValue() == null ? "" : getValue().toString() );
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

			// Transfer current value
			set(getValue());
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
		for( Input<T> input : myRemote )
		{
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
			wire.setSourceComponent( getOwningComponentId().toString() );
			wire.setSourceOutput( getId().toString() );
			wire.setTargetComponent( remote.getOwningComponentId().toString() );
			wire.setTargetInput( remote.getId().toString() );
			wire.setType( this.getClass().getSimpleName().replace( "Output", "Wire" ) );
			data.getWires().getWireDef().add( wire );
		}
	}
}
