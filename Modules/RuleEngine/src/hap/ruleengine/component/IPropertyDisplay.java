package hap.ruleengine.component;

import hap.ruleengine.parts.property.IntProperty;
import hap.ruleengine.parts.property.StringProperty;

public interface IPropertyDisplay
{
	void show( StringProperty property );

	void show( IntProperty property );
}
