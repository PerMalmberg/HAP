package hap.ruleengine.parts;

import java.util.UUID;

public abstract class ConnectionPoint implements IConnectionPoint
{
	private String myName;
	private final boolean myIsVisibleOnComponent;
	protected IComponent myParent;

	public ConnectionPoint( String name, IComponent parent, boolean isVisibleOnComponent )
	{
		myName = name;
		myParent = parent;
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

	@Override
	public UUID getComponentId()
	{
		return myParent.getId();
	}
}
