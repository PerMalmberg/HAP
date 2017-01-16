// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.parts;

import hap.ruleengine.parts.Wire.IWire;
import hap.ruleengine.parts.composite.CompositeComponent;
import hap.ruleengine.parts.data.ComponentDef;
import hap.ruleengine.parts.data.CompositeDef;
import hap.ruleengine.parts.input.BooleanInput;
import hap.ruleengine.parts.input.DoubleInput;
import hap.ruleengine.parts.input.Input;
import hap.ruleengine.parts.input.StringInput;
import hap.ruleengine.parts.output.BooleanOutput;
import hap.ruleengine.parts.output.DoubleOutput;
import hap.ruleengine.parts.output.StringOutput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public abstract class Component implements IComponent
{
	private final UUID myInstanceId;
	private final HashMap<UUID, BooleanInput> myBooleanInput = new HashMap<>();
	private final HashMap<UUID, BooleanOutput> myBooleanOutput = new HashMap<>();
	private final HashMap<UUID, DoubleInput> myDoubleInput = new HashMap<>();
	private final HashMap<UUID, DoubleOutput> myDoubleOutput = new HashMap<>();
	private final HashMap<UUID, StringInput> myStringInput = new HashMap<>();
	private final HashMap<UUID, StringOutput> myStringOutput = new HashMap<>();
	private String myName;
	private double X = 0.0;
	private double Y = 0.0;
	private boolean myIsVisualized = false;

	// Determines if the component may execute its (more advanced) internal logic
	// i.e. connect to external systems etc. This is always false when a component
	// is visualized in a component pallet.
	private boolean myExecutionState = false;

	public Component( UUID id, boolean executionAllowed )
	{
		myInstanceId = id;
		myExecutionState = executionAllowed;
	}

	@Override
	public void setExecutionState( boolean status )
	{
		myExecutionState = status;
	}

	@Override
	public boolean getExecutionState()
	{
		return myExecutionState;
	}

	// Called during creation of parts. Use it to add in- and outputs.
	public abstract void setup( CompositeComponent cc );

	public void addInput( BooleanInput input )
	{
		myBooleanInput.put( input.getId(), input );
	}

	public void addInput( DoubleInput input )
	{
		myDoubleInput.put( input.getId(), input );
	}

	public void addInput( StringInput input )
	{
		myStringInput.put( input.getId(), input );
	}

	public void addOutput( BooleanOutput output )
	{
		myBooleanOutput.put( output.getId(), output );
	}

	public void addOutput( DoubleOutput output )
	{
		myDoubleOutput.put( output.getId(), output );
	}

	public void addOutput( StringOutput output )
	{
		myStringOutput.put( output.getId(), output );
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
	public double getX()
	{
		return X;
	}

	@Override
	public void setX( double x )
	{
		X = x;
	}

	@Override
	public double getY()
	{
		return Y;
	}

	@Override
	public void setY( double y )
	{
		Y = y;
	}

	@Override
	public int getSubComponentCount()
	{
		return 0;
	}

	@Override
	public HashMap<UUID, BooleanOutput> getBooleanOutputs()
	{
		return myBooleanOutput;
	}

	@Override
	public HashMap<UUID, BooleanInput> getBooleanInputs()
	{
		return myBooleanInput;
	}

	@Override
	public HashMap<UUID, DoubleInput> getDoubleInputs()
	{
		return myDoubleInput;
	}

	@Override
	public HashMap<UUID, DoubleOutput> getDoubleOutputs()
	{
		return myDoubleOutput;
	}

	@Override
	public HashMap<UUID, StringInput> getStringInputs()
	{
		return myStringInput;
	}

	@Override
	public HashMap<UUID, StringOutput> getStringOutputs()
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
		return myName == null || myName.length() == 0 ? this.getClass().getSimpleName() : myName;
	}

	@Override
	public void setName( String name )
	{
		myName = name;
	}

	boolean loadComponentFromData( ComponentDef def )
	{
		myName = def.getName();
		X = def.getX();
		Y = def.getY();
		return true;
	}

	@Override
	public void store( CompositeDef data )
	{
		// A component stores itself and any wires on its outputs
		ComponentDef def = new ComponentDef();
		def.setInstanceId( getId().toString() );
		def.setName( getName() );
		def.setNativeType( this.getClass().getName() );
		def.setX( X );
		def.setY( Y );

		data.getComponents().getComponentDef().add( def );
		storeWires( data );
	}

	protected void storeWires( CompositeDef data )
	{
		for( BooleanOutput output : myBooleanOutput.values() )
		{
			output.store( data );
		}

		for( DoubleOutput output : myDoubleOutput.values() )
		{
			output.store( data );
		}

		for( StringOutput output : myStringOutput.values() )
		{
			output.store( data );
		}
	}

	@Override
	public List<IWire> getWires()
	{
		return new ArrayList<>();
	}

	@Override
	public boolean isVisualized()
	{
		return myIsVisualized;
	}

	@Override
	public void setVisualized() {
		myIsVisualized = true;
	}
}
