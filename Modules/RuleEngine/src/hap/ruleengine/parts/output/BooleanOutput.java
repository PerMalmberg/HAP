// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.parts.output;

import hap.ruleengine.parts.IComponent;
import hap.ruleengine.parts.IConnectionPoint;
import hap.ruleengine.parts.Wire.BooleanWire;
import hap.ruleengine.parts.Wire.IWire;
import hap.ruleengine.parts.Wire.Wire;
import hap.ruleengine.parts.composite.CompositeComponent;
import hap.ruleengine.parts.input.BooleanInput;

public class BooleanOutput extends Output<Boolean>
{
	public BooleanOutput( String name, IComponent parent, boolean isVisibleOnCompoent )
	{
		super( name, parent, false, isVisibleOnCompoent );
	}

	public BooleanOutput( String name, IComponent parent )
	{
		super( name, parent, false, true );
	}

	@Override
	public IWire connectTo( IConnectionPoint other, CompositeComponent cc )
	{
		IWire wire = null;
		if( other instanceof BooleanInput )
		{
			BooleanInput input = (BooleanInput) other;
			wire = new BooleanWire( Wire.createDef( this, input, BooleanWire.class.getSimpleName() ) );
			boolean res = wire.connect( cc );
			if( !res ) {
				wire = null;
			}

		}

		return wire;
	}
}
