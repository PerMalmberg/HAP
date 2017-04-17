// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.parts.node;

import hap.ruleengine.parts.*;
import hap.ruleengine.parts.composite.CompositeComponent;
import hap.ruleengine.parts.input.DoubleInput;
import hap.ruleengine.parts.output.DoubleOutput;

import java.util.UUID;


public class DoubleOutputNode extends Component
{
	private DoubleOutput out;

	public DoubleOutputNode( UUID id, boolean executionState )
	{
		super( id, executionState );
	}

	@Override
	public void setup( CompositeComponent cc )
	{
		DoubleInput in = new DoubleInput( "", getId(), this );
		out = new DoubleOutput( getName(), getId(), this, false );
		addInput( in );
		addOutput( out );

		// Add our output to the parent output so it can be accessed from outside.
		cc.addOutput( out );
	}

	@Override
	public void inputChanged( DoubleInput input )
	{
		out.set( input.getValue() );
	}
}
