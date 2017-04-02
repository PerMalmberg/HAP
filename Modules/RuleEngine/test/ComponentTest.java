// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import hap.SysUtil;
import hap.ruleengine.component.bool.Nand;
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
	private final ComponentFactory f = new ComponentFactory(Paths.get( SysUtil.getDirectoryOfJar( ComponentTest.class ), "RuleEngine/ComponentLibrary"));

	private CompositeComponent loadComponent( String data )
	{
		File src = Paths.get( SysUtil.getDirectoryOfJar( ComponentTest.class ), "RuleEngine/ComponentLibrary/" + data ).toFile();
		return f.create( src, UUID.randomUUID() );
	}

	@Test
	public void createComponentFromNameTest()
	{
		CompositeComponent cc = new CompositeComponent( UUID.randomUUID(), null, false );
		IComponent c = f.createFromName( "hap.ruleengine.component.bool.And", cc, false );
		assertNotNull( c );
		assertEquals( 1, cc.getSubComponentCount() );
	}

	@Test
	public void loadCompositeWithImportTest()
	{
		IComponent c = loadComponent( "LoadCompositeWithImportTest.xml" );
		assertTrue( c != null );
		assertTrue( c.getId() != null );
		assertTrue( c.getSubComponentCount() == 4 );

		BooleanInput a = c.getBooleanInputs().get( UUID.fromString( "11111111-ddb5-4934-90f0-924c9bb2ed95" ) );
		BooleanInput b = c.getBooleanInputs().get( UUID.fromString( "22222222-ddb5-4934-90f0-924c9bb2eeee" ) );
		BooleanOutput out = c.getBooleanOutputs().get( UUID.fromString( "44444444-ddb5-4934-90f0-924c9bb2edaa" ) );

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
	public void testAndComponent()
	{
		CompositeComponent c = loadComponent( "TestAndComponent.xml" );

		BooleanInput a = c.getBooleanInputs().get( UUID.fromString( "4e11d2cc-57a2-4ba4-9d35-aed40e73844e" ) );
		BooleanInput b = c.getBooleanInputs().get( UUID.fromString( "bf7db3d9-5002-4774-a4a2-db8bfc6728d0" ) );
		BooleanOutput out = c.getBooleanOutputs().get( UUID.fromString( "cf9cb9cb-fc5d-4122-a5de-550c0566b728" ) );

		assertFalse( Read2Way( a, b, out, false, false ) );
		assertFalse( Read2Way( a, b, out, true, false ) );
		assertFalse( Read2Way( a, b, out, false, true ) );
		assertTrue( Read2Way( a, b, out, true, true ) );
	}

	@Test
	public void testNandComponent()
	{
		CompositeComponent c = loadComponent( "TestNandComponent.xml" );

		BooleanInput a = c.getBooleanInputs().get( UUID.fromString( "0a75869e-9c6f-4e4f-b4b3-d55311374038" ) );
		BooleanInput b = c.getBooleanInputs().get( UUID.fromString( "ccd9b243-649f-4af1-ae96-53f968b47fc7" ) );
		BooleanOutput out = c.getBooleanOutputs().get( UUID.fromString( "35a1ac39-2cb4-45f1-a762-f8a423f0f54f" ) );

		assertTrue( out.getValue() );
		assertTrue( Read2Way( a, b, out, false, false) );
		assertTrue( Read2Way( a, b, out, true, false) );
		assertFalse( Read2Way( a, b, out, true, true) );
		assertTrue( Read2Way( a, b, out, false, true) );
	}

	@Test
	public void testAddComponent()
	{
		CompositeComponent c = loadComponent( "TestAddComponent.xml" );

		DoubleInput a = c.getDoubleInputs().get( UUID.fromString( "0f766bed-0362-4226-8e1a-8367ad1febdb" ) );
		DoubleInput b = c.getDoubleInputs().get( UUID.fromString( "260f070a-bcfb-4731-a4ec-956b028a5781" ) );
		DoubleOutput out = c.getDoubleOutputs().get( UUID.fromString( "4a62c1dd-cd7a-4676-b355-e1089dbabe45" ) );

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
		StringInput a = c.getStringInputs().get( UUID.fromString( "98dd743c-4f36-43f6-a7ce-04e89fe169d4" ) );
		StringInput b = c.getStringInputs().get( UUID.fromString( "0f5e9b0d-0546-43d1-be1c-c3cd3fe20fa0" ) );
		StringOutput out = c.getStringOutputs().get( UUID.fromString( "dc52864f-3365-4db7-8c13-5d6addd3626c" ) );

		assertEquals( "", out.getValue() );
		a.set( "" );
		assertEquals( "", out.getValue() );
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
		File src = Paths.get( SysUtil.getDirectoryOfJar( ComponentTest.class ), "RuleEngine/ComponentLibrary/TestAddComponent.xml" ).toFile();

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
		DoubleInput a = c.getDoubleInputs().get( UUID.fromString( "260f070a-bcfb-4731-a4ec-956b028a5781" ) );
		DoubleInput b = c.getDoubleInputs().get( UUID.fromString( "0f766bed-0362-4226-8e1a-8367ad1febdb" ) );
		DoubleOutput out = c.getDoubleOutputs().get( UUID.fromString( "4a62c1dd-cd7a-4676-b355-e1089dbabe45" ) );

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

	private boolean Read2Way( BooleanInput a, BooleanInput b, BooleanOutput out, boolean aValue, boolean bValue )
	{
		a.set( aValue );
		b.set( bValue );
		return out.getValue();
	}

}

