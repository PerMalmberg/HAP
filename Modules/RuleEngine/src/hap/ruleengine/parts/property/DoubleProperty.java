package hap.ruleengine.parts.property;


import hap.ruleengine.parts.Component;

public class DoubleProperty extends BaseProperty<Double>
{
	public DoubleProperty( String header, String key, Double defaultValue, Component c)
	{
		super( header, key, defaultValue, c);
	}

	@Override
	public void setValue( Double value )
	{
		myComp.setProperty( myKey, value );
	}

	@Override
	public Double getValue()
	{
		return myComp.getProperty( myKey, myDefaultValue );
	}
}
