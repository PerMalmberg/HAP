// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.parts;

import hap.SysUtil;
import hap.ruleengine.parts.composite.CompositeComponent;
import hap.ruleengine.parts.data.ComponentDef;
import hap.ruleengine.parts.data.CompositeDef;
import org.jetbrains.annotations.NotNull;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;

public class ComponentFactory implements IComponentFactory
{
	private HashMap<String, String> myFiles = new HashMap<>();
	private final Stack<String> myLoadedFiles = new Stack<>();
	private final Logger myLogger = Logger.getLogger( ComponentFactory.class.getName() );
	private final Path myComponentLibrary;
	public static final Path STANDARD_LIBRARY = Paths.get( SysUtil.getFullOrRelativePath( CompositeComponent.class, "ComponentLibrary" ) );

	public ComponentFactory()
	{
		this( null );
	}

	public ComponentFactory( Path componentLibrary )
	{
		myComponentLibrary = componentLibrary;
	}

	///////////////////////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////////////////////
	@Override
	public CompositeComponent create( File compositeFile, UUID uid )
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
					cc = create( data, uid, compositeFile );
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
	public CompositeComponent create( File compositeFile, UUID uid, CompositeComponent parent )
	{
		CompositeComponent cc = create( compositeFile, uid );
		if( cc != null) {
			parent.addComponent( cc );
		}

		return cc;
	}


	///////////////////////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////////////////////
	@Override
	public IComponent create( ComponentDef def, CompositeComponent cc )
	{
		Component c = null;

		try
		{
			// Create an instance of the parts with the id and data as arguments.
			Class<?> componentClass = Class.forName( def.getNativeType() );
			if( componentClass != null )
			{
				Constructor<?> ctor = componentClass.getConstructor( UUID.class, boolean.class );
				c = (Component) ctor.newInstance( UUID.fromString( def.getInstanceId() ), false );

				// Let the parts load itself
				if( ! c.loadComponentFromData( def ) )
				{
					c = null;
				}
				else
				{
					cc.addComponent( c );
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
	public IComponent createFromName( @NotNull String componentType, @NotNull CompositeComponent parent )
	{
		ComponentDef def = new ComponentDef();
		def.setNativeType( componentType );
		def.setInstanceId( UUID.randomUUID().toString() );
		return create( def, parent );
	}

	///////////////////////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////////////////////
	@Override
	public File findImport( String fileName )
	{
		// The component library is expected to be located in the folder "ComponentLibrary" relative to
		// where the application is running from, unless specified in the constructor.
		Path lib = getLibPath();

		ArrayList<Path> foundFiles = new ArrayList<>();

		try
		{
			Files.walk( lib )
					.filter( Files::isRegularFile )
					.forEach(
							path ->
							{
								if( fileName.equals( path.getFileName().toString() ) )
								{
									foundFiles.add( path );
								}
							} );
		}
		catch( IOException e )
		{
			e.printStackTrace();
		}

		return foundFiles.size() == 1 ? foundFiles.get( 0 ).toFile() : null;
	}

	///////////////////////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////////////////////
	private Path getLibPath()
	{
		Path lib;
		if( myComponentLibrary != null )
		{
			lib = myComponentLibrary;
		}
		else
		{
			lib = STANDARD_LIBRARY;
		}
		return lib;
	}

	///////////////////////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////////////////////
	@Override
	public List<String> getAvailableImports()
	{
		ArrayList<String> imports = new ArrayList<>();

		try
		{
			Path lib = getLibPath();
			Files.walk( lib )
					.filter( Files::isRegularFile )
					.forEach(
							path ->
							{
								if( path.getFileName().toString().endsWith( ".cc" ) )
								{
									imports.add( path.toString() );
								}
							} );
		}
		catch( IOException e )
		{
			e.printStackTrace();
		}

		return imports;
	}

	///////////////////////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////////////////////
	@Override
	public CompositeComponent create( String compositeData, UUID compositeUid, File sourceFile )
	{
		CompositeComponent cc = null;

		try
		{
			JAXBContext jc = JAXBContext.newInstance( CompositeDef.class );
			Unmarshaller u = jc.createUnmarshaller();

			SchemaFactory schemaFactory = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
			String schemaPath = SysUtil.getFullOrRelativePath( CompositeComponent.class, "RuleEngine\\CompositeDefinition.xsd" );
			Schema schema = schemaFactory.newSchema( new File( schemaPath ) );
			u.setSchema( schema );

			CompositeDef data = (CompositeDef) u.unmarshal( new InputSource( new StringReader( compositeData ) ) );
			cc = new CompositeComponent( compositeUid, sourceFile.getName(), false );
			if( cc.loadCompositeFromData( data, this ) )
			{
				cc.setName( buildNameFromSourceFile(sourceFile) );
			}
			else
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

	private String buildNameFromSourceFile( File sourceFile )
	{
		String name = sourceFile.getName();
		if( name.endsWith( ".cc" )) {
			name = name.substring( 0, name.lastIndexOf( ".cc" ) );
		}
		return name;
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
