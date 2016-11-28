// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package ruleengine.parts;

import ruleengine.parts.input.BooleanInput;
import ruleengine.parts.input.DoubleInput;
import ruleengine.parts.input.Input;
import ruleengine.parts.input.StringInput;
import ruleengine.parts.output.BooleanOutput;
import ruleengine.parts.output.DoubleOutput;
import ruleengine.parts.output.StringOutput;

import java.util.HashMap;
import java.util.UUID;

public interface IComponent
{
	HashMap<String, BooleanOutput> getBooleanOutputs();

	HashMap<String, BooleanInput> getBooleanInputs();

	HashMap<String, StringInput> getStringInputs();

	HashMap<String, StringOutput> getStringOutputs();

	HashMap<String, DoubleInput> getDoubleInputs();

	HashMap<String, DoubleOutput> getDoubleOutputs();

	UUID getId();

	int getSubComponentCount();

	void inputChanged( Input<?> input );

	String getName();

}
