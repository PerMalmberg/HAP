package hap.ruleengine.parts;

import hap.ruleengine.parts.Wire.IWire;
import hap.ruleengine.parts.composite.CompositeComponent;

public interface IConnectionPoint
{
	String getName();

	void setName( String name );

	// Returns true if the connection point should be visible when the component is visualized.
	boolean isVisible();

	// Connects two connection points, if their sub classes allows it
	IWire connectTo( IConnectionPoint other, CompositeComponent cc );

	IComponent getParent();
}
