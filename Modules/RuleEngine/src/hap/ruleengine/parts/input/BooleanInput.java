// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.parts.input;

import hap.ruleengine.parts.Component;
import hap.ruleengine.parts.IComponent;
import hap.ruleengine.parts.IConnectionPoint;
import hap.ruleengine.parts.Wire.BooleanWire;
import hap.ruleengine.parts.Wire.IWire;
import hap.ruleengine.parts.Wire.Wire;
import hap.ruleengine.parts.composite.CompositeComponent;
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
	public IWire connectTo( IConnectionPoint other, CompositeComponent cc )
	{
		IWire wire = null;
		if( other instanceof BooleanOutput )
		{
			BooleanOutput output = (BooleanOutput) other;
			wire = new BooleanWire( Wire.createDef( output, this, BooleanWire.class.getSimpleName() ) );
			boolean res = wire.connect( cc );
			if( !res ) {
				wire = null;
			}
		}

		return wire;
	}
}
