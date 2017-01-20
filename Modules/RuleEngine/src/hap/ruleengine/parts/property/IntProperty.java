package hap.ruleengine.parts.property;


import hap.ruleengine.parts.Component;

public class IntProperty extends BaseProperty<Integer>
{
	public IntProperty( String header, String key, Integer defaultValue, Component c )
	{
		super( header, key, defaultValue, c );
	}

	@Override
	public void setValue( Integer value )
	{
		myComp.setProperty( myKey, value );
	}

	@Override
	public Integer getValue()
	{
		return myComp.getProperty( myKey, myDefaultValue );
	}
}
