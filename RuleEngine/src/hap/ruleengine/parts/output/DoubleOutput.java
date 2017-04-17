// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.parts.output;

import hap.ruleengine.parts.IComponent;
import hap.ruleengine.parts.IConnectionPoint;
import hap.ruleengine.parts.Wire.DoubleWire;
import hap.ruleengine.parts.Wire.IWire;
import hap.ruleengine.parts.Wire.Wire;
import hap.ruleengine.parts.composite.CompositeComponent;
import hap.ruleengine.parts.input.DoubleInput;

import java.util.UUID;

public class DoubleOutput extends Output<Double>
{
	public DoubleOutput( String name, UUID id, IComponent parent, boolean isVisibleWhenParentIsVisualized )
	{
		super( name, id, parent, Double.NaN, isVisibleWhenParentIsVisualized );
	}

	public DoubleOutput( String name, UUID id, IComponent parent )
	{
		super( name, id, parent, Double.NaN, true );
	}

	@Override
	public IWire connectTo( IConnectionPoint other, CompositeComponent cc )
	{
		IWire wire = null;
		if( other instanceof DoubleInput )
		{
			DoubleInput input = (DoubleInput) other;
			wire = new DoubleWire( Wire.createDef( this, input, DoubleWire.class.getSimpleName() ) );
			boolean res = wire.connect( cc );
			if( !res ) {
				wire = null;
			}
		}

		return wire;
	}
}
