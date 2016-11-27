// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package ruleengine.component.bool;

import ruleengine.parts.BooleanInput;
import ruleengine.parts.BooleanOutput;
import ruleengine.parts.Component;
import ruleengine.parts.composite.CompositeComponent;

import java.util.UUID;

public class And extends Component
{
	private BooleanInput inA;
	private BooleanInput inB;
	private BooleanOutput out;

	public And( UUID id )
	{
		super( id );
	}

	@Override
	public void setup( CompositeComponent cc )
	{
		inA = new BooleanInput( getId(), "A", this );
		inB = new BooleanInput( getId(), "B", this );
		out = new BooleanOutput( getId(), "Out", this );
		addInput( inA );
		addInput( inB );
		addOutput( out );
	}

	@Override
	public void inputChanged( BooleanInput input )
	{
		out.set( inA.getValue() && inB.getValue() );
	}
}
