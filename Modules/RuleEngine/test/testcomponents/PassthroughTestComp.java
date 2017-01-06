// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package testcomponents;

import hap.ruleengine.parts.input.BooleanInput;
import hap.ruleengine.parts.output.BooleanOutput;
import hap.ruleengine.parts.Component;
import hap.ruleengine.parts.composite.CompositeComponent;

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
		BooleanInput in = new BooleanInput( "In", this );
		out = new BooleanOutput( "Out", this );
		addInput( in );
		addOutput( out );
	}

	@Override
	public void inputChanged( BooleanInput input )
	{
		out.set( input.getValue() );
	}


}
