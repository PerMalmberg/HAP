// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package ruleengine.parts.output;

import ruleengine.parts.IComponent;

public class StringOutput extends Output<String>
{
	public StringOutput( String name, IComponent parent )
	{
		super( name, parent, null );
	}
}
