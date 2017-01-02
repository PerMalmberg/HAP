// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.parts;

import hap.ruleengine.parts.data.CompositeDef;
import hap.ruleengine.parts.input.BooleanInput;
import hap.ruleengine.parts.input.DoubleInput;
import hap.ruleengine.parts.input.Input;
import hap.ruleengine.parts.input.StringInput;
import hap.ruleengine.parts.output.BooleanOutput;
import hap.ruleengine.parts.output.DoubleOutput;
import hap.ruleengine.parts.output.StringOutput;

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

	double getX();
	void setX(double x);
	double getY();
	void setY(double y);

	String getName();
	void setName( String name );

	void store( CompositeDef data );
}
