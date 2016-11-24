// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package ruleengine.component;

import java.io.File;

public interface IComponentFactory
{
	IComponent create( String componentData );

	IComponent create( File componentData );
}
