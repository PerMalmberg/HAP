// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package ruleengine.component.numeric;

import ruleengine.parts.Component;
import ruleengine.parts.input.DoubleInput;
import ruleengine.parts.output.DoubleOutput;
import ruleengine.parts.composite.CompositeComponent;

import java.util.UUID;


public class Add extends Component
{
	private DoubleInput inA;
	private DoubleInput inB;
	private DoubleOutput out;

	public Add( UUID id )
	{
		super( id );
	}

	@Override
	public void setup( CompositeComponent cc )
	{
		inA = new DoubleInput( "A", this );
		inB = new DoubleInput( "B", this );
		out = new DoubleOutput( "Out", this );
		addInput( inA );
		addInput( inB );
		addOutput( out );
	}

	@Override
	public void inputChanged( DoubleInput input )
	{
		if( !Double.isNaN( inA.getValue() ) && !Double.isNaN( inB.getValue() ) )
		{
			out.set( inA.getValue() + inB.getValue() );
		}
	}
}
