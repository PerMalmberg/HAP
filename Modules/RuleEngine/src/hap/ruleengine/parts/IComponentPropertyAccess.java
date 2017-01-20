package hap.ruleengine.parts;

public interface IComponentPropertyAccess
{
	void setProperty( String key, String value );

	void setProperty( String key, int value );

	void setProperty( String key, boolean value );

	void setProperty( String key, double value );

	String getProperty( String key, String defaultValue );

	int getProperty( String key, int defaultValue );

	boolean getProperty( String key, boolean defaultValue );

	double getProperty( String key, double defaultValue );
}
