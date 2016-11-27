// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package ruleengine.component.node;

import ruleengine.component.BooleanInput;
import ruleengine.component.BooleanOutput;
import ruleengine.component.Component;
import ruleengine.component.composite.CompositeComponent;

import java.util.UUID;


public class BooleanInputNode extends Component
{
	private BooleanOutput out;

	public BooleanInputNode( UUID id )
	{
		super( id );
	}

	@Override
	public void setup( CompositeComponent cc )
	{
		// Nodes share ID with their parent
		BooleanInput in = new BooleanInput( getId(), getName(), this );
		out = new BooleanOutput( getId(), getName(), this );

		addInput( in );
		addOutput( out );

		// Add our input to the parent output so it can be accessed from outside.
		cc.addInput( in );
	}

	@Override
	public void inputChanged( BooleanInput input )
	{
		out.set( input.getValue() );
	}
}
