// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package ruleengine.component;

import java.util.UUID;

public interface IOutput
{
	void disconnectAll();
	String getName();
	UUID getId();
}
