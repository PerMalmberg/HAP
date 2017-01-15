// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.parts.node;

import hap.ruleengine.parts.input.BooleanInput;
import hap.ruleengine.parts.output.BooleanOutput;
import hap.ruleengine.parts.Component;
import hap.ruleengine.parts.composite.CompositeComponent;

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
		BooleanInput in = new BooleanInput( getName(), getId(), this, false );
		out = new BooleanOutput("", getId(), this );

		addInput( in );
		addOutput( out );

		// Add our input to the parent so it can be accessed from outside.
		cc.addInput( in );
	}

	@Override
	public void inputChanged( BooleanInput input )
	{
		out.set( input.getValue() );
	}
}
