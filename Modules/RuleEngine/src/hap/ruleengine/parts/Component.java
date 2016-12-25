// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.parts;

import hap.ruleengine.parts.composite.CompositeComponent;
import hap.ruleengine.parts.data.ComponentDef;
import hap.ruleengine.parts.input.BooleanInput;
import hap.ruleengine.parts.input.DoubleInput;
import hap.ruleengine.parts.input.Input;
import hap.ruleengine.parts.input.StringInput;
import hap.ruleengine.parts.output.BooleanOutput;
import hap.ruleengine.parts.output.DoubleOutput;
import hap.ruleengine.parts.output.StringOutput;

import java.util.HashMap;
import java.util.UUID;

public abstract class Component implements IComponent
{
	private final UUID myInstanceId;
	protected final HashMap<String, BooleanInput> myBooleanInput = new HashMap<>();
	protected final HashMap<String, BooleanOutput> myBooleanOutput = new HashMap<>();
	protected final HashMap<String, DoubleInput> myDoubleInput = new HashMap<>();
	protected final HashMap<String, DoubleOutput> myDoubleOutput = new HashMap<>();
	protected final HashMap<String, StringInput> myStringInput = new HashMap<>();
	protected final HashMap<String, StringOutput> myStringOutput = new HashMap<>();
	private String myName;

	public Component( UUID id )
	{
		myInstanceId = id;
	}

	// Called during creation of parts. Use it to add in- and outputs.
	public abstract void setup( CompositeComponent cc );

	public void addInput( BooleanInput input )
	{
		myBooleanInput.put( input.getName(), input );
	}

	public void addInput( DoubleInput input )
	{
		myDoubleInput.put( input.getName(), input );
	}

	public void addInput( StringInput input )
	{
		myStringInput.put( input.getName(), input );
	}

	public void addOutput( BooleanOutput output )
	{
		myBooleanOutput.put( output.getName(), output );
	}
	public void addOutput( DoubleOutput output )
	{
		myDoubleOutput.put( output.getName(), output );
	}
	public void addOutput( StringOutput output )
	{
		myStringOutput.put( output.getName(), output );
	}

	public void inputChanged( Input<?> input )
	{
		input.signal( this );
	}

	public void inputChanged( BooleanInput input )
	{
	}

	public void inputChanged( DoubleInput input )
	{
	}

	public void inputChanged( StringInput input )
	{
	}


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
	public HashMap<String, DoubleInput> getDoubleInputs()
	{
		return myDoubleInput;
	}

	@Override
	public HashMap<String, DoubleOutput> getDoubleOutputs()
	{
		return myDoubleOutput;
	}

	@Override
	public HashMap<String, StringInput> getStringInputs()
	{
		return myStringInput;
	}

	@Override
	public HashMap<String, StringOutput> getStringOutputs()
	{
		return myStringOutput;
	}

	@Override
	public UUID getId()
	{
		return myInstanceId;
	}

	@Override
	public String getName()
	{
		// A top-level composite has no name
		return myName == null ? "" : myName;
	}

	@Override
	public void setName( String name )
	{
		myName = name;
	}

	public boolean loadComponentFromData( ComponentDef def )
	{
		myName = def.getName();

		return true;
	}
}
