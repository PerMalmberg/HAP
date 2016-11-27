// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package ruleengine.parts;

import java.util.UUID;

public class BooleanInput extends Input<Boolean>
{
	public BooleanInput( UUID id, String name, IComponent parent )
	{
		super( id, name, parent, false );
	}

	@Override
	public void signal( Component component )
	{
		component.inputChanged( this );
	}
}
