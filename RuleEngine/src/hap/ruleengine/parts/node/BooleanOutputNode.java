// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.parts.node;

import hap.ruleengine.parts.Component;
import hap.ruleengine.parts.composite.CompositeComponent;
import hap.ruleengine.parts.input.BooleanInput;
import hap.ruleengine.parts.output.BooleanOutput;

import java.util.UUID;


public class BooleanOutputNode extends Component
{
	private BooleanOutput out;

	public BooleanOutputNode( UUID id, boolean executionState)
	{
		super( id, executionState );
	}

	@Override
	public void setup( CompositeComponent cc )
	{
		BooleanInput in = new BooleanInput( "", getId(), this );
		out = new BooleanOutput( getName(), getId(), this, false );

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
