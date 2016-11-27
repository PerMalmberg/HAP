// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package ruleengine.parts;

import ruleengine.parts.composite.CompositeComponent;

public interface IWire
{
	boolean connect( CompositeComponent cc );
}
