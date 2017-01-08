// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.parts.output;

import hap.ruleengine.parts.data.CompositeDef;
import hap.ruleengine.parts.IConnectionPoint;

public interface IOutput extends IConnectionPoint
{
	void store( CompositeDef data );
}
