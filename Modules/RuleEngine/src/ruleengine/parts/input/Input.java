// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package ruleengine.parts.input;

import ruleengine.parts.Component;
import ruleengine.parts.IComponent;

public abstract class Input<T> implements IInput
{
	private T myValue;
	private String myName;
	private IComponent myParent;

	public Input( String name, IComponent parent, T defaultValue )
	{
		myName = name;
		myParent = parent;
		myValue = defaultValue;
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


	public abstract void signal( Component component );
}
