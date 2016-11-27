// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package testcomponents;

import ruleengine.parts.BooleanInput;
import ruleengine.parts.BooleanOutput;
import ruleengine.parts.Component;
import ruleengine.parts.composite.CompositeComponent;

import java.util.UUID;

public class PassthroughTestComp extends Component
{
	public PassthroughTestComp( UUID id)
	{
		super(id);
	}

	private BooleanOutput out;

	@Override
	public void setup( CompositeComponent cc )
	{
		BooleanInput in = new BooleanInput( getId(), "In", this );
		out = new BooleanOutput( getId(), "Out", this );
		addInput( in );
		addOutput( out );
	}

	@Override
	public void inputChanged( BooleanInput input )
	{
		out.set( input.getValue() );
	}


}
