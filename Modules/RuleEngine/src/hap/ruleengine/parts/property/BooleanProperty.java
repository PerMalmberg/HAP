package hap.ruleengine.parts.property;


import hap.ruleengine.parts.Component;

public class BooleanProperty extends BaseProperty<Boolean>
{
	public BooleanProperty( String header, String key, Boolean defaultValue, Component c)
	{
		super( header, key, defaultValue, c);
	}

	@Override
	public void setValue( Boolean value )
	{
		myComp.setProperty( myKey, value );
	}

	@Override
	public Boolean getValue()
	{
		return myComp.getProperty( myKey, myDefaultValue );
	}
}
