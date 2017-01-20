package hap.ruleengine.parts.property;


import hap.ruleengine.parts.Component;

public class StringProperty extends BaseProperty<String>
{
	public StringProperty( String header, String key, String defaultValue, Component c)
	{
		super(header, key, defaultValue, c);
	}

	@Override
	public void setValue( String value )
	{
		myComp.setProperty( myKey, value );
	}

	@Override
	public String getValue()
	{
		return myComp.getProperty( myKey, myDefaultValue );
	}
}
