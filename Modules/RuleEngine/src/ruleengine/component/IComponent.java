// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package ruleengine.component;

import java.util.HashMap;
import java.util.UUID;

public interface IComponent
{
	HashMap<String, BooleanOutput> getBooleanOutputs();

	HashMap<String, BooleanInput> getBooleanInputs();

	UUID getId();

	int getSubComponentCount();

	void inputChanged( Input<?> input );

	String getName();

}
