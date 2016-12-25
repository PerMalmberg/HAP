// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.component.bool;

import hap.ruleengine.parts.input.BooleanInput;
import hap.ruleengine.parts.output.BooleanOutput;
import hap.ruleengine.parts.Component;
import hap.ruleengine.parts.composite.CompositeComponent;

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
