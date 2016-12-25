// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.parts.output;

import hap.ruleengine.parts.IComponent;
import hap.ruleengine.parts.input.Input;

import java.util.ArrayList;
import java.util.List;

public class Output<T> implements IOutput
{
	private T myValue;
	private List<Input<T>> myRemote = new ArrayList<>();
	private String myName;
	private IComponent myParent;
	private int myCallCount = 0;

	public Output( String name, IComponent parent, T defaultValue )
	{
		myName = name;
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
	public String getName()
	{
		return myName;
	}

}
