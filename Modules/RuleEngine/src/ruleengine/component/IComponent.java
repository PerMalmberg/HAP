// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package ruleengine.component;

import ruleengine.xpath.IXPathReader;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public interface IComponent
{
	HashMap<String, IOutput> getOutputs();
	HashMap<String, IInput> getInputs();
	UUID getId();
	List<IWire> getWires();
	boolean loadComponentFromData( String componentData, IComponentFactory factory, IXPathReader reader );
}
