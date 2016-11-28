// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

import hap.SysUtil;
import org.junit.Test;
import ruleengine.parts.*;
import ruleengine.parts.composite.CompositeComponent;
import ruleengine.parts.input.BooleanInput;
import ruleengine.parts.input.DoubleInput;
import ruleengine.parts.input.StringInput;
import ruleengine.parts.output.BooleanOutput;
import ruleengine.parts.output.DoubleOutput;
import ruleengine.parts.output.StringOutput;

import java.io.File;
import java.nio.file.Paths;

import static org.junit.Assert.*;


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
		a.set( true );
		assertFalse( out.getValue() );
		b.set( true );
		assertTrue( out.getValue() );
		a.set( false );
		assertFalse( out.getValue() );
		b.set( false );
		assertFalse( out.getValue() );
	}

	@Test
	public void testAddComponent()
	{
		CompositeComponent c = loadComponent( "TestAddComponent.xml" );

		DoubleInput a = c.getDoubleInputs().get( "A" );
		DoubleInput b = c.getDoubleInputs().get( "B" );
		DoubleOutput out = c.getDoubleOutputs().get( "Out" );

		assertTrue( Double.isNaN( out.getValue() ) );
		a.set( 0d );
		assertTrue( Double.isNaN( out.getValue() ) );
		b.set( 0d );
		assertEquals( 0, out.getValue(), 0.0 );
		a.set( 5.5 );
		assertEquals( 5.5, out.getValue(), 0.0 );
		b.set( 4.5 );
		assertEquals( 10, out.getValue(), 0.0 );
	}

	@Test
	public void testConcatenateComponent()
	{
		CompositeComponent c = loadComponent( "TestConcatenateComponent.xml" );

		StringInput a = c.getStringInputs().get( "A" );
		StringInput b = c.getStringInputs().get( "B" );
		StringOutput out = c.getStringOutputs().get( "Out" );

		assertEquals( null, out.getValue() );
		a.set( "" );
		assertEquals( null, out.getValue() );
		b.set( "" );
		assertEquals( "", out.getValue() );
		a.set( "ABCD" );
		assertEquals( "ABCD", out.getValue() );
		b.set( "EFGH" );
		assertEquals( "ABCDEFGH", out.getValue() );
	}

}

