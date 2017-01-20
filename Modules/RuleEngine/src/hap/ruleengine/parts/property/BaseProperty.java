package hap.ruleengine.parts.property;


import hap.ruleengine.parts.Component;

public abstract class BaseProperty<T> implements IPropertyContainer
{
	protected final Component myComp;
	protected final String myKey;
	protected final T myDefaultValue;
	private final String myHeader;

	BaseProperty( String header, String key, T defaultValue, Component c )
	{
		myHeader = header;
		myKey = key;
		myDefaultValue = defaultValue;
		myComp = c;
	}

	public String getHeader()
	{
		return myHeader;
	}

	public abstract void setValue(T value);
	public abstract T getValue();
}
