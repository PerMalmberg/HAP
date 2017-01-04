// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.parts.Wire;

import hap.ruleengine.parts.composite.CompositeComponent;

import java.util.UUID;

public interface IWire
{
	boolean connect( CompositeComponent cc );
	void disconnect();
	UUID getSourceComponent();
	String getSourceOutput();
	UUID getTargetComponent();
	String getTargetInput();
}
