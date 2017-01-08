// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.parts.input;

import hap.ruleengine.parts.Component;
import hap.ruleengine.parts.IComponent;
import hap.ruleengine.parts.IConnectionPoint;
import hap.ruleengine.parts.Wire.DoubleWire;
import hap.ruleengine.parts.Wire.IWire;
import hap.ruleengine.parts.Wire.Wire;
import hap.ruleengine.parts.composite.CompositeComponent;
import hap.ruleengine.parts.output.DoubleOutput;

public class DoubleInput extends Input<Double>
{
	public DoubleInput( String name, IComponent parent, boolean isVisibleInComponent )
	{
		super( name, parent, Double.NaN, isVisibleInComponent );
	}

	public DoubleInput( String name, IComponent parent )
	{
		super( name, parent, Double.NaN, true );
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
		if( other instanceof DoubleOutput )
		{
			DoubleOutput output = (DoubleOutput) other;
			wire = new DoubleWire( Wire.createDef( output, this, DoubleWire.class.getSimpleName() ) );
			boolean res = wire.connect( cc );
			if( !res ) {
				wire = null;
			}
		}

		return wire;
	}
}
