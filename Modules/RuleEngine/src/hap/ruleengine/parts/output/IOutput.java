// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.parts.output;

import hap.ruleengine.parts.data.CompositeDef;

public interface IOutput
{
	void disconnectAll();
	String getName();

	void store( CompositeDef data );
}
