// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package ruleengine.parts.node;

import ruleengine.parts.*;
import ruleengine.parts.composite.CompositeComponent;
import ruleengine.parts.input.StringInput;
import ruleengine.parts.output.StringOutput;

import java.util.UUID;


public class StringInputNode extends Component
{
	private StringOutput out;

	public StringInputNode( UUID id )
	{
		super( id );
	}

	@Override
	public void setup( CompositeComponent cc )
	{
		StringInput in = new StringInput( getName(), this );
		out = new StringOutput( getName(), this );

		addInput( in );
		addOutput( out );

		// Add our input to the parent output so it can be accessed from outside.
		cc.addInput( in );
	}

	@Override
	public void inputChanged( StringInput input )
	{
		out.set( input.getValue() );
	}
}
