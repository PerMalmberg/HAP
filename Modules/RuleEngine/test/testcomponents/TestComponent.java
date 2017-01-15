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
		super( id, false );
	}

	@Override
	public void setup( CompositeComponent cc )
	{
		addInput( new BooleanInput( "A", UUID.fromString( "218b3710-6d47-4730-bb7d-2d822daf6552" ), this ) );
	}
}
