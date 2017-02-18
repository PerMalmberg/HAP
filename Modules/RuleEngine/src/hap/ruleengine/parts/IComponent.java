// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.parts;

import hap.ruleengine.component.IPropertyDisplay;
import hap.ruleengine.parts.Wire.IWire;
import hap.ruleengine.parts.data.CompositeDef;
import hap.ruleengine.parts.input.BooleanInput;
import hap.ruleengine.parts.input.DoubleInput;
import hap.ruleengine.parts.input.Input;
import hap.ruleengine.parts.input.StringInput;
import hap.ruleengine.parts.output.BooleanOutput;
import hap.ruleengine.parts.output.DoubleOutput;
import hap.ruleengine.parts.output.StringOutput;
import javafx.beans.property.Property;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public interface IComponent
{
	HashMap<UUID, BooleanOutput> getBooleanOutputs();

	HashMap<UUID, BooleanInput> getBooleanInputs();

	HashMap<UUID, StringInput> getStringInputs();

	HashMap<UUID, StringOutput> getStringOutputs();

	HashMap<UUID, DoubleInput> getDoubleInputs();

	HashMap<UUID, DoubleOutput> getDoubleOutputs();

	UUID getId();

	int getSubComponentCount();

	void inputChanged( Input<?> input );

	double getX();

	void setX( double x );

	double getY();

	void setY( double y );

	String getName();

	void setName( String name );

	void store( CompositeDef data );

	List<IWire> getWires();

	boolean isVisualized();

	void setVisualized();

	void setExecutionState( boolean status );

	boolean getExecutionState();

	void showProperties( IPropertyDisplay display );

	void tick();

	void tearDown();
}
