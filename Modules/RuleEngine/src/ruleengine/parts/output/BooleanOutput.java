// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package ruleengine.parts.output;

import ruleengine.parts.IComponent;

public class BooleanOutput extends Output<Boolean>
{
	public BooleanOutput( String name, IComponent parent )
	{
		super( name, parent, false );
	}
}
