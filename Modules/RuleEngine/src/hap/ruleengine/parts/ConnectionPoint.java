package hap.ruleengine.parts;

import java.util.UUID;

public abstract class ConnectionPoint implements IConnectionPoint
{
	private String myName;
	private UUID myId;
	private final boolean myIsVisibleWhenParentIsVisualized;
	protected IComponent myParent;

	public ConnectionPoint( String name, UUID id, IComponent parent, boolean isVisibleWhenParentIsVisualized )
	{
		myName = name;
		myId = id;
		myParent = parent;
		myIsVisibleWhenParentIsVisualized = isVisibleWhenParentIsVisualized;
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
	public UUID getId()
	{
		return myId;
	}

	@Override
	public void setId(UUID id)
	{
		myId = id;
	}

	@Override
	public boolean isVisible()
	{
		boolean res;

		if( myParent.isVisualized()) {
			res = myIsVisibleWhenParentIsVisualized;
		}
		else {
			res = true;
		}

		return res;
	}

	@Override
	public IComponent getParent()
	{
		return myParent;
	}
}
