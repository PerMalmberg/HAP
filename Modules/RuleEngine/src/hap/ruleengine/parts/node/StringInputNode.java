// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.parts.node;

import hap.ruleengine.parts.*;
import hap.ruleengine.parts.composite.CompositeComponent;
import hap.ruleengine.parts.input.StringInput;
import hap.ruleengine.parts.output.StringOutput;

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
		StringInput in = new StringInput( getName(), getId(), this, false );
		out = new StringOutput( "", getId(), this );

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
