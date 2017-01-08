// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.parts;

import hap.ruleengine.parts.composite.CompositeComponent;
import hap.ruleengine.parts.data.ComponentDef;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.UUID;

public interface IComponentFactory
{
	CompositeComponent create( String compositeData, UUID compositeUid, File sourceFile );

	CompositeComponent create( File componentData, UUID uid );

	IComponent create( ComponentDef c, CompositeComponent cc );

	IComponent createFromName( @NotNull String componentType, @NotNull CompositeComponent parent );

	// All composite components are stored in the same folder structure (i.e. 'the component library'). As such it is possible to
	// locate any imported file by its name.
	File findImport( String fileName);
}
