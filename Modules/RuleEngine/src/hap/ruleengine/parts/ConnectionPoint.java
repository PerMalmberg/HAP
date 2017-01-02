package hap.ruleengine.parts;

public class ConnectionPoint implements IConnectionPoint
{
	private String myName;
	private final boolean myIsVisibleOnComponent;

	public ConnectionPoint( String name, boolean isVisibleOnComponent )
	{
		myName = name;
		myIsVisibleOnComponent = isVisibleOnComponent;
	}

	@Override
	public String getName()
	{
		return myName;
	}

	@Override
	public void setName( String name )
	{
		myName = name;
	}

	@Override
	public boolean isVisibleOnComponent()
	{
		return myIsVisibleOnComponent;
	}
}
