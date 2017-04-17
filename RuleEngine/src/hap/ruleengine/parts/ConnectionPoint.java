package hap.ruleengine.parts;

import hap.ruleengine.parts.composite.CompositeComponent;

import java.util.ArrayList;
import java.util.UUID;

public abstract class ConnectionPoint implements IConnectionPoint
{
	private String myName;
	private UUID myId;
	private final boolean myIsVisibleWhenParentIsVisualized;
	private IComponent myParent;
	protected final ArrayList<IValueChangeReceiver> valueChangeReceivers = new ArrayList<>();

	// Used to report the owning component. This is the same as the owner of the component the connection point
	// is a part of, except when we act as an input/output to a composite.
	private UUID myOwningComponent;

	public ConnectionPoint( String name, UUID id, IComponent parent, boolean isVisibleWhenParentIsVisualized )
	{
		myName = name;
		myId = id;
		myOwningComponent = parent.getId();
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
	public void setId( UUID id )
	{
		myId = id;
	}

	@Override
	public boolean isVisible()
	{
		boolean res;

		res = ! myParent.isVisualized() || myIsVisibleWhenParentIsVisualized;

		return res;
	}

	public void setOwningComposite( CompositeComponent cc )
	{
		myOwningComponent = cc.getId();
	}

	public UUID getOwningComponentId()
	{
		return myOwningComponent;
	}

	@Override
	public void subscribeToValueChanges( IValueChangeReceiver receiver )
	{
		valueChangeReceivers.add( receiver );
		notifyValueSubscribers();
	}

	@Override
	public void unsubscribeToValueChanges( IValueChangeReceiver receiver )
	{
		if( valueChangeReceivers.contains( receiver ) )
		{
			valueChangeReceivers.remove( receiver );
		}
	}

	@Override
	public void notifyValueSubscribers()
	{
	}
}
