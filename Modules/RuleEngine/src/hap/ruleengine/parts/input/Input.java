// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.parts.input;

import hap.ruleengine.parts.Component;
import hap.ruleengine.parts.ConnectionPoint;
import hap.ruleengine.parts.IComponent;

import java.util.UUID;

public abstract class Input<T> extends ConnectionPoint implements IInput
{
	private T myValue;

	private IComponent myParent;
	private boolean myIsConnected = false;

	Input( String name, UUID id, IComponent parent, T defaultValue, boolean isVisibleWhenParentIsVisualized )
	{
		super( name, id, parent, isVisibleWhenParentIsVisualized );
		myParent = parent;
		myValue = defaultValue;
	}

	public void set( T value )
	{
		myValue = value;
		myParent.inputChanged( this );
	}

	@Override
	public void disconnectAll()
	{
		// Nothing to disconnect in an input
	}

	public T getValue()
	{
		return myValue;
	}

	@Override
	public IComponent getParent()
	{
		return myParent;
	}

	public abstract void signal( Component component );

	public void markConnected()
	{
		myIsConnected = true;
	}

	public void markDisconnected()
	{
		myIsConnected = false;
	}

	public boolean isConnected()
	{
		return myIsConnected;
	}
}
