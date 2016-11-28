// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package ruleengine.component.string;

import ruleengine.parts.Component;
import ruleengine.parts.input.StringInput;
import ruleengine.parts.output.StringOutput;
import ruleengine.parts.composite.CompositeComponent;

import java.util.UUID;


public class Concatenate extends Component
{
	private StringInput inA;
	private StringInput inB;
	private StringOutput out;

	public Concatenate( UUID id )
	{
		super( id );
	}

	@Override
	public void setup( CompositeComponent cc )
	{
		inA = new StringInput( "A", this );
		inB = new StringInput( "B", this );
		out = new StringOutput( "Out", this );
		addInput( inA );
		addInput( inB );
		addOutput( out );
	}

	@Override
	public void inputChanged( StringInput input )
	{
		if( inA.getValue() != null && inB.getValue() != null )
		{
			out.set( inA.getValue().concat( inB.getValue() ) );
		}
	}
}
