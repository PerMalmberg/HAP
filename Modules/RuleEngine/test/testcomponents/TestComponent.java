// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package testcomponents;

import ruleengine.component.BooleanInput;
import ruleengine.component.Component;
import ruleengine.component.composite.CompositeComponent;

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
		addInput( new BooleanInput( getId(), "A", this ) );
	}

	@Override
	public void inputChanged( BooleanInput input )
	{

	}
}
