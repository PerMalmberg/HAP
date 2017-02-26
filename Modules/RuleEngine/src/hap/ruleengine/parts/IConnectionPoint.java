package hap.ruleengine.parts;

import hap.ruleengine.parts.Wire.IWire;
import hap.ruleengine.parts.composite.CompositeComponent;

import java.util.UUID;

public interface IConnectionPoint
{
	String getName();

	void setName( String name );

	UUID getId();

	void setId( UUID id );

	// Returns true if the connection point should be visible when the component is visualized.
	boolean isVisible();

	// Connects two connection points, if their sub classes allows it
	IWire connectTo( IConnectionPoint other, CompositeComponent cc );

	void disconnectAll();

	void subscribeToValueChanges( IValueChangeReceiver receiver );
	void unsubscribeToValueChanges( IValueChangeReceiver receiver );

	void notifyValueSubscribers();
}
