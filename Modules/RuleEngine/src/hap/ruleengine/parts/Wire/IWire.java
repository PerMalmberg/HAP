// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.parts.Wire;

import hap.ruleengine.parts.composite.CompositeComponent;

public interface IWire
{
	boolean connect( CompositeComponent cc );
	void disconnect();
}
