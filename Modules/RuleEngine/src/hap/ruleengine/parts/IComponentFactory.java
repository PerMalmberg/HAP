// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.parts;

import hap.ruleengine.parts.composite.CompositeComponent;
import hap.ruleengine.parts.data.ComponentDef;

import java.io.File;

public interface IComponentFactory
{
	CompositeComponent create( String componentData );

	CompositeComponent create( File componentData );

	IComponent create( ComponentDef c, CompositeComponent cc  );
}