// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

import hap.SysUtil;
import org.junit.Test;
import ruleengine.component.ComponentFactory;
import ruleengine.component.IComponent;
import ruleengine.component.composite.CompositeComponent;

import java.io.File;
import java.nio.file.Paths;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class ComponentTest
{
	private final ComponentFactory f = new ComponentFactory();

	private CompositeComponent loadComponent( String data )
	{
		File src = Paths.get( SysUtil.getDirectoryOfJar( ComponentTest.class ), "RuleEngine/" + data ).toFile();
		return f.create( src );
	}

	@Test
	public void loadComposite()
	{
		IComponent c = loadComponent( "MinimalComposite.xml" );
		assertTrue( c != null );
		assertTrue( c.getId() != null );
	}


	@Test
	public void loadCompositeWithImportTest()
	{
		IComponent c = loadComponent( "LoadCompositeWithImportTest.xml" );
		assertTrue( c != null );
		assertTrue( c.getId() != null );
		assertTrue( c.getSubComponentCount() == 1 );
	}


	@Test
	public void loadCompositeWithRecursiveImportTest()
	{
		IComponent c = loadComponent( "LoadCompositeWithRecursiveImportTest.xml" );
		assertTrue( c == null );
	}

	@Test
	public void testPassthroughComponent()
	{
		CompositeComponent c = loadComponent( "PassthroughTestComp.xml" );

		c.getBooleanInputs().get( "MyInput" ).set( false );
		assertFalse( c.getBooleanOutputs().get( "MyOutput" ).getValue() );
		c.getBooleanInputs().get( "MyInput" ).set( true );
		assertTrue( c.getBooleanOutputs().get( "MyOutput" ).getValue() );
		c.getBooleanInputs().get( "MyInput" ).set( false );
		assertFalse( c.getBooleanOutputs().get( "MyOutput" ).getValue() );
	}
}

