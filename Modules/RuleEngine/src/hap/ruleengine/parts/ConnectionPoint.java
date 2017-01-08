package hap.ruleengine.parts;

public abstract class ConnectionPoint implements IConnectionPoint
{
	private String myName;
	private final boolean myIsVisibleWhenParentIsVisualized;
	protected IComponent myParent;

	public ConnectionPoint( String name, IComponent parent, boolean isVisibleWhenParentIsVisualized )
	{
		myName = name;
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
