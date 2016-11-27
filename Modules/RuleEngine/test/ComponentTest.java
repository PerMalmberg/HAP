// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

import hap.SysUtil;
import org.junit.Test;
import ruleengine.parts.BooleanInput;
import ruleengine.parts.BooleanOutput;
import ruleengine.parts.ComponentFactory;
import ruleengine.parts.IComponent;
import ruleengine.parts.composite.CompositeComponent;

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

	@Test
	public void testAndComponent()
	{
		CompositeComponent c = loadComponent( "TestAndComponent.xml" );

		BooleanInput a = c.getBooleanInputs().get( "A" );
		BooleanInput b = c.getBooleanInputs().get( "B" );
		BooleanOutput out = c.getBooleanOutputs().get( "Out" );

		assertFalse( out.getValue() );
		a.set(true);
		assertFalse( out.getValue() );
		b.set(true);
		assertTrue( out.getValue() );
		a.set(false);
		assertFalse( out.getValue() );
		b.set(false);
		assertFalse( out.getValue() );
	}

}

