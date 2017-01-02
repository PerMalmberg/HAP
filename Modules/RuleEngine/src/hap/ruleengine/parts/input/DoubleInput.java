// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.parts.input;

import hap.ruleengine.parts.Component;
import hap.ruleengine.parts.IComponent;

public class DoubleInput extends Input<Double>
{
	public DoubleInput( String name, IComponent parent, boolean isVisibleInComponent )
	{
		super( name, parent, Double.NaN, isVisibleInComponent );
	}

	public DoubleInput( String name, IComponent parent )
	{
		super( name, parent, Double.NaN, true );
	}

	@Override
	public void signal( Component component )
	{
		component.inputChanged( this );
	}
}
