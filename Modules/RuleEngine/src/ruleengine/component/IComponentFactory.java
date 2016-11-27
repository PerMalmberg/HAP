// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package ruleengine.component;

import ruleengine.component.composite.CompositeComponent;
import ruleengine.component.data.ComponentDef;

import java.io.File;

public interface IComponentFactory
{
	CompositeComponent create( String componentData );

	CompositeComponent create( File componentData );

	IComponent create( ComponentDef c, CompositeComponent cc  );
}
