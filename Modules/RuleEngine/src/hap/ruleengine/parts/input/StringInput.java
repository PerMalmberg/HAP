// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.parts.input;

import hap.ruleengine.parts.Component;
import hap.ruleengine.parts.IComponent;

public class StringInput extends Input<String>
{
	public StringInput( String name, IComponent parent )
	{
		super( name, parent, null );
	}

	@Override
	public void signal( Component component )
	{
		component.inputChanged( this );
	}
}