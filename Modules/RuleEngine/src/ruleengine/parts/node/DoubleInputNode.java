// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package ruleengine.parts.node;

import ruleengine.parts.*;
import ruleengine.parts.composite.CompositeComponent;
import ruleengine.parts.input.DoubleInput;
import ruleengine.parts.output.DoubleOutput;

import java.util.UUID;


public class DoubleInputNode extends Component
{
	private DoubleOutput out;

	public DoubleInputNode( UUID id )
	{
		super( id );
	}

	@Override
	public void setup( CompositeComponent cc )
	{
		DoubleInput in = new DoubleInput( getName(), this );
		out = new DoubleOutput( getName(), this );

		addInput( in );
		addOutput( out );

		// Add our input to the parent output so it can be accessed from outside.
		cc.addInput( in );
	}

	@Override
	public void inputChanged( DoubleInput input )
	{
		out.set( input.getValue() );
	}
}
