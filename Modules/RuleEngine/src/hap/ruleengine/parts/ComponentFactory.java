// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.parts;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import hap.ruleengine.parts.composite.CompositeComponent;
import hap.ruleengine.parts.data.ComponentDef;
import hap.ruleengine.parts.data.CompositeDef;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Stack;
import java.util.UUID;
import java.util.logging.Logger;

public class ComponentFactory implements IComponentFactory
{
	private HashMap<String, String> myFiles = new HashMap<>();
	private final Stack<String> myLoadedFiles = new Stack<>();
	private final Logger myLogger = Logger.getLogger( ComponentFactory.class.getName() );

	///////////////////////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////////////////////
	@Override
	public CompositeComponent create( File compositeFile )
	{
		CompositeComponent cc = null;

		if( ! compositeFile.isAbsolute() )
		{
			// Try to find the file in the directory the last loaded file is located.
			if( myLoadedFiles.size() > 0 )
			{
				File last = new File( myLoadedFiles.lastElement() );
				compositeFile = Paths.get( last.getParent(), compositeFile.toString() ).toFile();
			}
			else
			{
				compositeFile = null;
			}
		}

		if( compositeFile != null )
		{
			// If a file ever loads a file that it itself was loaded by we must abort
			boolean crossLoadFound = myLoadedFiles.contains( compositeFile.getAbsolutePath() );

			if( ! crossLoadFound )
			{
				myLoadedFiles.add( compositeFile.getAbsolutePath() );
				String data = loadFile( compositeFile );
				if( data != null )
				{
					cc = create( data );
				}
				myLoadedFiles.remove( myLoadedFiles.lastElement() );
			}

		}

		return cc;
	}

	///////////////////////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////////////////////
	@Override
	public IComponent create( ComponentDef def, CompositeComponent cc  )
	{
		Component c = null;

		try
		{
			// Create an instance of the parts with the id and data as arguments.
			Class<?> componentClass = Class.forName( def.getNativeType() );
			if( componentClass != null )
			{
				Constructor<?> ctor = componentClass.getConstructor( UUID.class );
				c = (Component) ctor.newInstance( UUID.fromString( def.getInstanceId() ) );

				// Let the parts load itself
				if( ! c.loadComponentFromData( def ) )
				{
					c = null;
				}
				else {
					c.setup( cc );
				}
			}
		}
		catch( ClassNotFoundException | NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e )
		{
			myLogger.severe( e.getMessage() );
		}

		return c;
	}

	///////////////////////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////////////////////
	@Override
	public CompositeComponent create( String compositeData )
	{
		CompositeComponent cc = null;

		try
		{
			JAXBContext jc = JAXBContext.newInstance( CompositeDef.class );
			Unmarshaller u = jc.createUnmarshaller();

			// TODO: QQQ schema location to be determined at runtime
			SchemaFactory schemaFactory = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
			Schema schema = schemaFactory.newSchema( new File( "d:\\git\\HAP\\Modules\\RuleEngine\\schema\\CompositeDefinition.xsd" ) );
			u.setSchema( schema );

			CompositeDef data = (CompositeDef) u.unmarshal( new InputSource( new StringReader( compositeData ) ) );
			cc = new CompositeComponent();
			if( ! cc.loadCompositeFromData( data, this ) )
			{
				cc = null;
			}
		}
		catch( JAXBException | SAXException e )
		{
			e.printStackTrace();
		}


		return cc;
	}


	///////////////////////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////////////////////
	private String loadFile( File f )
	{
		// Check the cache first
		String contents = myFiles.get( f.getAbsolutePath() );

		if( contents == null )
		{
			// No cache hit, try to load the contents from disk
			try( FileInputStream fis = new FileInputStream( f ) )
			{
				byte[] data = new byte[(int) f.length()];
				int readData = fis.read( data );
				if( readData > 0 )
				{
					contents = new String( data, "UTF-8" );
					// Store for later use
					myFiles.put( f.getAbsolutePath(), contents );
				}
			}
			catch( IOException e )
			{
				myLogger.severe( e.toString() );
			}
		}

		return contents;
	}
}
