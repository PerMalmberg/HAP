// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

import hap.SysUtil;
import org.junit.Test;
import ruleengine.component.ComponentFactory;
import ruleengine.component.IComponent;

import java.io.File;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;


public class ComponentTest
{
	private final ComponentFactory f = new ComponentFactory();

	@Test
	public void loadComponent()
	{
		File src = Paths.get( SysUtil.getDirectoryOfJar( ComponentTest.class ), "RuleEngine/LoadTest.xml" ).toFile();
		IComponent c = f.create( src );
		assertTrue( c != null );
		assertTrue( c.getId() != null );
	}

	@Test
	public void loadWithRecursiveImportTest()
	{
		File src = Paths.get( SysUtil.getDirectoryOfJar( ComponentTest.class ), "RuleEngine/LoadWithRecursiveImportTest.xml" ).toFile();
		IComponent c = f.create( src );
		assertTrue( c == null );
	}
}
