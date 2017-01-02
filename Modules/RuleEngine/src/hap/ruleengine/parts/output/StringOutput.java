// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.parts.output;

import hap.ruleengine.parts.IComponent;

public class StringOutput extends Output<String>
{
	public StringOutput( String name, IComponent parent , boolean isVisibleOnComponent)
	{
		super( name, parent, null, isVisibleOnComponent );
	}

	public StringOutput( String name, IComponent parent )
	{
		super( name, parent, null, true );
	}
}
