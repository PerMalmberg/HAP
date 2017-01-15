// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.parts.node;

import hap.ruleengine.parts.*;
import hap.ruleengine.parts.composite.CompositeComponent;
import hap.ruleengine.parts.input.StringInput;
import hap.ruleengine.parts.output.StringOutput;

import java.util.UUID;


public class StringOutputNode extends Component
{
	private StringOutput out;

	public StringOutputNode( UUID id, boolean executionState )
	{
		super( id, executionState );
	}

	@Override
	public void setup( CompositeComponent cc )
	{
		StringInput in = new StringInput( "", getId(), this );
		out = new StringOutput( getName(), getId(), this, false );
		addInput( in );
		addOutput( out );

		// Add our output to the parent output so it can be accessed from outside.
		cc.addOutput( out );
	}

	@Override
	public void inputChanged( StringInput input )
	{
		out.set( input.getValue() );
	}
}
