// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package ruleengine.component;

import ruleengine.component.composite.CompositeComponent;

public interface IWire
{
	boolean connect( CompositeComponent cc );
}
