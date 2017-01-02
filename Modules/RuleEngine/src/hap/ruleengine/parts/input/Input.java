// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.parts.input;

import hap.ruleengine.parts.Component;
import hap.ruleengine.parts.ConnectionPoint;
import hap.ruleengine.parts.IComponent;

public abstract class Input<T> extends ConnectionPoint implements IInput
{
	private T myValue;

	private IComponent myParent;

	Input( String name, IComponent parent, T defaultValue, boolean visibleOnComponent )
	{
		super(name, visibleOnComponent);
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
	public IComponent getParent() { return myParent; }

	public abstract void signal( Component component );
}
