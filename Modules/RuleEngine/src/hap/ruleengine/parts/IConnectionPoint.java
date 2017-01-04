package hap.ruleengine.parts;

import java.util.ArrayList;
import java.util.UUID;

public interface IConnectionPoint
{
	String getName();
	void setName( String name );
	// Returns true if the connection point should be visible when the component is visualized.
	boolean isVisible();

}
