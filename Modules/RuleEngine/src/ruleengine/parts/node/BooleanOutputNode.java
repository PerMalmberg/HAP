// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package ruleengine.parts.node;

import ruleengine.parts.input.BooleanInput;
import ruleengine.parts.output.BooleanOutput;
import ruleengine.parts.Component;
import ruleengine.parts.composite.CompositeComponent;

import java.util.UUID;


public class BooleanOutputNode extends Component
{
	private BooleanOutput out;

	public BooleanOutputNode( UUID id )
	{
		super( id );
	}

	@Override
	public void setup( CompositeComponent cc )
	{
		BooleanInput in = new BooleanInput( getName(), this );
		out = new BooleanOutput( getName(), this );
		addInput( in );
		addOutput( out );

		// Add our output to the parent output so it can be accessed from outside.
		cc.addOutput( out );
	}

	@Override
	public void inputChanged( BooleanInput input )
	{
		out.set( input.getValue() );
	}
}
