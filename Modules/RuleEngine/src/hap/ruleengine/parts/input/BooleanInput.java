// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.parts.input;

import hap.ruleengine.parts.Component;
import hap.ruleengine.parts.IComponent;
import hap.ruleengine.parts.IConnectionPoint;
import hap.ruleengine.parts.Wire.BooleanWire;
import hap.ruleengine.parts.Wire.IWire;
import hap.ruleengine.parts.Wire.Wire;
import hap.ruleengine.parts.output.BooleanOutput;

public class BooleanInput extends Input<Boolean>
{
	public BooleanInput( String name, IComponent parent, boolean isVisibleInComponent )
	{
		super( name, parent, false, isVisibleInComponent );
	}

	public BooleanInput( String name, IComponent parent )
	{
		super( name, parent, false, true );
	}

	@Override
	public void signal( Component component )
	{
		component.inputChanged( this );
	}

	@Override
	public IWire connectTo( IConnectionPoint other )
	{
		IWire wire = null;
		if( other instanceof BooleanOutput )
		{
			BooleanOutput output = (BooleanOutput) other;
			if( output.connect( this ) )
			{
				wire = new BooleanWire( Wire.createDef( output, this, BooleanWire.class.getSimpleName() ) );
			}
		}

		return wire;
	}
}
