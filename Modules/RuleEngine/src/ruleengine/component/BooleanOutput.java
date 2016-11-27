// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package ruleengine.component;

import java.util.UUID;

public class BooleanOutput extends Output<Boolean>
{
	public BooleanOutput( UUID id, String name, IComponent parent )
	{
		super( id, name, parent );
	}
}
