// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package ruleengine.component;

import java.util.UUID;

public abstract class Input<T> implements IInput
{
	private T myValue;
	private String myName;
	private IComponent myParent;
	private UUID myId;

	public Input( UUID id, String name, IComponent parent )
	{
		myName = name;
		myParent = parent;
		myId = id;
	}

	public void set( T value )
	{
		myValue = value;
		myParent.inputChanged( this );
	}

	public T getValue()
	{
		return myValue;
	}

	@Override
	public String getName()
	{
		return myName;
	}

	@Override
	public UUID getId()
	{
		return myId;
	}

	public abstract void signal( Component component );
}
