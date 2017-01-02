package hap.ruleengine.parts;

public interface IConnectionPoint
{
	String getName();
	void setName( String name );
	// Returns true if the connection point should be visible when the component is visualized.
	boolean isVisibleOnComponent();
}
