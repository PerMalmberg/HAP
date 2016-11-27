// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package ruleengine.component;

import ruleengine.component.composite.CompositeComponent;
import ruleengine.component.data.ComponentDef;

import java.util.HashMap;
import java.util.UUID;

public abstract class Component implements IComponent
{
	private final UUID myInstanceId;
	protected final HashMap<String, BooleanInput> myBooleanInput = new HashMap<>();
	protected final HashMap<String, BooleanOutput> myBooleanOutput = new HashMap<>();
	private String myName;

	public Component( UUID id )
	{
		myInstanceId = id;
	}

	// Called during creation of component. Use it to add in- and outputs.
	public abstract void setup( CompositeComponent cc );

	public void addInput( BooleanInput input )
	{
		myBooleanInput.put( input.getName(), input );
	}

	public void addOutput( BooleanOutput output )
	{
		myBooleanOutput.put( output.getName(), output );
	}

	public void inputChanged( Input<?> input )
	{
		input.signal( this );
	}

	public abstract void inputChanged( BooleanInput input );

	@Override
	public int getSubComponentCount()
	{
		return 0;
	}

	@Override
	public HashMap<String, BooleanOutput> getBooleanOutputs()
	{
		return myBooleanOutput;
	}

	@Override
	public HashMap<String, BooleanInput> getBooleanInputs()
	{
		return myBooleanInput;
	}

	@Override
	public UUID getId()
	{
		return myInstanceId;
	}

	@Override
	public String getName()
	{
		return myName;
	}

	public boolean loadComponentFromData( ComponentDef def )
	{
		myName = def.getName();

		return true;
	}
}
