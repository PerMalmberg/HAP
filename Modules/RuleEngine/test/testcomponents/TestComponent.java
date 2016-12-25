// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package testcomponents;

import hap.ruleengine.parts.input.BooleanInput;
import hap.ruleengine.parts.Component;
import hap.ruleengine.parts.composite.CompositeComponent;

import java.util.UUID;

public class TestComponent extends Component
{
	public TestComponent( UUID id )
	{
		super( id );
	}

	@Override
	public void setup( CompositeComponent cc )
	{
		addInput( new BooleanInput( "A", this ) );
	}
}
