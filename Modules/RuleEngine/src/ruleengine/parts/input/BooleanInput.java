// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package ruleengine.parts.input;

import ruleengine.parts.Component;
import ruleengine.parts.IComponent;

public class BooleanInput extends Input<Boolean>
{
	public BooleanInput( String name, IComponent parent )
	{
		super( name, parent, false );
	}

	@Override
	public void signal( Component component )
	{
		component.inputChanged( this );
	}
}
