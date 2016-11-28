// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package ruleengine.parts.output;

import ruleengine.parts.IComponent;

public class DoubleOutput extends Output<Double>
{
	public DoubleOutput( String name, IComponent parent )
	{
		super( name, parent, Double.NaN );
	}
}
