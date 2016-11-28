// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package ruleengine.component.bool;

import ruleengine.parts.input.BooleanInput;
import ruleengine.parts.output.BooleanOutput;
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
		inA = new BooleanInput( "A", this );
		inB = new BooleanInput( "B", this );
		out = new BooleanOutput( "Out", this );
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
