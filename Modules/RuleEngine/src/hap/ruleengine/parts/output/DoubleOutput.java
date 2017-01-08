// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.parts.output;

import hap.ruleengine.parts.IComponent;
import hap.ruleengine.parts.IConnectionPoint;
import hap.ruleengine.parts.Wire.DoubleWire;
import hap.ruleengine.parts.Wire.IWire;
import hap.ruleengine.parts.Wire.Wire;
import hap.ruleengine.parts.input.DoubleInput;

public class DoubleOutput extends Output<Double>
{
	public DoubleOutput( String name, IComponent parent, boolean isVisibleWhenParentIsVisualized )
	{
		super( name, parent, Double.NaN, isVisibleWhenParentIsVisualized );
	}

	public DoubleOutput( String name, IComponent parent )
	{
		super( name, parent, Double.NaN, true );
	}

	@Override
	public IWire connectTo( IConnectionPoint other )
	{
		IWire wire = null;
		if( other instanceof DoubleInput )
		{
			DoubleInput input = (DoubleInput) other;
			if( connect( input ) )
			{
				wire = new DoubleWire( Wire.createDef( this, input, DoubleWire.class.getSimpleName() ) );
			}
		}

		return wire;
	}
}
