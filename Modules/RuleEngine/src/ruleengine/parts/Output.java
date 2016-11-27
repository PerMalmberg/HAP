// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package ruleengine.parts;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Output<T> implements IOutput
{
	private T myValue;
	private List<Input<T>> myRemote = new ArrayList<>();
	private String myName;
	private UUID myId;
	private IComponent myParent;
	private int myCallCount = 0;

	public Output( UUID id, String name, IComponent parent, T defaultValue )
	{
		myId = id;
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

	void connect( Input<T> remote )
	{
		myRemote.add( remote );
	}

	void disconnect( Input<T> remote )
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

	@Override
	public UUID getId()
	{
		return myId;
	}
}
