// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package ruleengine.parts.Wire;

import ruleengine.parts.composite.CompositeComponent;

public interface IWire
{
	boolean connect( CompositeComponent cc );
	void disconnect();
}
