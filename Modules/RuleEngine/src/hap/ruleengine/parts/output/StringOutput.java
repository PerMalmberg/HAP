// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.parts.output;

import hap.ruleengine.parts.IComponent;
import hap.ruleengine.parts.IConnectionPoint;
import hap.ruleengine.parts.Wire.IWire;
import hap.ruleengine.parts.Wire.StringWire;
import hap.ruleengine.parts.Wire.Wire;
import hap.ruleengine.parts.composite.CompositeComponent;
import hap.ruleengine.parts.input.StringInput;

public class StringOutput extends Output<String>
{
	public StringOutput( String name, IComponent parent, boolean isVisibleWhenParentIsVisualized )
	{
		super( name, parent, null, isVisibleWhenParentIsVisualized );
	}

	public StringOutput( String name, IComponent parent )
	{
		super( name, parent, null, true );
	}

	@Override
	public IWire connectTo( IConnectionPoint other , CompositeComponent cc)
	{
		IWire wire = null;
		if( other instanceof StringInput )
		{
			StringInput input = (StringInput) other;
			wire = new StringWire( Wire.createDef( this, input, StringWire.class.getSimpleName() ) );
			boolean res = wire.connect( cc );
			if( !res ) {
				wire = null;
			}
		}

		return wire;
	}
}
