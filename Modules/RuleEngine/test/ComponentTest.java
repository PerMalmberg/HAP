// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import hap.SysUtil;
import hap.ruleengine.parts.ComponentFactory;
import hap.ruleengine.parts.CompositeSerializer;
import hap.ruleengine.parts.IComponent;
import hap.ruleengine.parts.composite.CompositeComponent;
import hap.ruleengine.parts.data.CompositeDef;
import hap.ruleengine.parts.input.BooleanInput;
import hap.ruleengine.parts.input.DoubleInput;
import hap.ruleengine.parts.input.StringInput;
import hap.ruleengine.parts.output.BooleanOutput;
import hap.ruleengine.parts.output.DoubleOutput;
import hap.ruleengine.parts.output.StringOutput;
import org.junit.Test;
import org.xml.sax.InputSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.nio.file.Paths;
import java.util.UUID;

import static org.junit.Assert.*;


public class ComponentTest
{
	private final ComponentFactory f = new ComponentFactory();

	private CompositeComponent loadComponent( String data )
	{
		File src = Paths.get( SysUtil.getDirectoryOfJar( ComponentTest.class ), "RuleEngine/" + data ).toFile();
		return f.create( src, UUID.randomUUID() );
	}

	@Test
	public void loadCompositeWithImportTest()
	{
		IComponent c = loadComponent( "LoadCompositeWithImportTest.xml" );
		assertTrue( c != null );
		assertTrue( c.getId() != null );
		assertTrue( c.getSubComponentCount() == 4 );

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

		testConcatenate( c );
	}

	private void testConcatenate( CompositeComponent c )
	{
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

	@Test
	public void testStoreComponent()
	{
		CompositeComponent c = loadComponent( "TestConcatenateComponent.xml" );
		File output = Paths.get( SysUtil.getDirectoryOfJar( ComponentTest.class ), "RuleEngine/stored.xml" ).toFile();
		if( output.exists() )
		{
			assertTrue( output.delete() );
		}

		CompositeSerializer cs = new CompositeSerializer();
		assertTrue( cs.serialize( c, output ) );

		CompositeComponent cat = f.create( output.getAbsoluteFile(), UUID.randomUUID() );
		testConcatenate( cat );

		assertTrue( output.delete() );
	}

	@Test
	public void testLoadViaJSON() throws JAXBException, IOException
	{
		// Read the XML data
		JAXBContext jc = JAXBContext.newInstance( CompositeDef.class );
		Unmarshaller u = jc.createUnmarshaller();
		File src = Paths.get( SysUtil.getDirectoryOfJar( ComponentTest.class ), "RuleEngine/TestAddComponent.xml" ).toFile();

		String contents = null;
		try( FileInputStream fis = new FileInputStream( src ) )
		{
			byte[] data = new byte[(int) src.length()];
			int readData = fis.read( data );
			if( readData > 0 )
			{
				contents = new String( data, "UTF-8" );
			}
		}

		assertNotNull( contents );

		// Create the JAXB data object
		CompositeDef rawData = (CompositeDef) u.unmarshal( new InputSource( new StringReader( contents ) ) );

		// Transform the JAXB to JSON
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable( SerializationFeature.INDENT_OUTPUT );
		StringWriter sw = new StringWriter();
		mapper.writeValue( sw, rawData );
		String json = sw.toString();

		// Create JAXB from JSON
		CompositeDef fromJSON = mapper.readValue( json, CompositeDef.class );
		Marshaller m = jc.createMarshaller();

		// Write JAXB to XML-file
		File xmlFile = Paths.get( SysUtil.getDirectoryOfJar( ComponentTest.class ), "fromJson.xml" ).toFile();
		if( xmlFile.exists() )
		{
			assertTrue( xmlFile.delete() );
		}

		try( FileOutputStream fs = new FileOutputStream( xmlFile ) )
		{
			m.marshal( fromJSON, fs );
		}

		// Create composite component from written data
		CompositeComponent c = f.create( xmlFile, UUID.randomUUID() );

		if( xmlFile.exists() )
		{
			assertTrue( xmlFile.delete() );
		}

		// Perform tests on CC
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

}

