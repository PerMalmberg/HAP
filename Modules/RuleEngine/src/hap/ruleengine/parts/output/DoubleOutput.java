// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.parts.output;

import hap.ruleengine.parts.IComponent;

public class DoubleOutput extends Output<Double>
{
	public DoubleOutput( String name, IComponent parent, boolean isVisibleOnComponent )
	{
		super( name, parent, Double.NaN, isVisibleOnComponent );
	}

	public DoubleOutput( String name, IComponent parent )
	{
		super( name, parent, Double.NaN, true );
	}
}
