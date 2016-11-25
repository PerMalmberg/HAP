// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package ruleengine.component;

import org.xml.sax.InputSource;
import ruleengine.xpath.IXPathReader;
import ruleengine.xpath.XPathReader;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;

public class ComponentFactory implements IComponentFactory
{
	private HashMap<String, String> myFiles = new HashMap<>();
	private final XPathFactory myXPathFac = XPathFactory.newInstance();
	private final IXPathReader xPathReader = new XPathReader();
	private final Stack<String> myLoadedFiles = new Stack<>();
	private final Logger myLogger = Logger.getLogger( ComponentFactory.class.getName() );

	///////////////////////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////////////////////
	@Override
	public IComponent create( File componentData )
	{
		IComponent c = null;

		if( ! componentData.isAbsolute() )
		{
			// Try to find the file in the directory the last loaded file is located.
			if( myLoadedFiles.size() > 0 )
			{
				File last = new File( myLoadedFiles.lastElement() );
				componentData = Paths.get( last.getParent(), componentData.toString() ).toFile();
			}
			else
			{
				componentData = null;
			}
		}

		if( componentData != null )
		{
			// If a file ever loads a file that it itself was loaded by we must abort
			boolean crossLoadFound = myLoadedFiles.contains( componentData.getAbsolutePath() );

			if( ! crossLoadFound )
			{
				myLoadedFiles.add( componentData.getAbsolutePath() );
				String data = loadFile( componentData );
				if( data != null)
				{
					c = create( data );
				}
				myLoadedFiles.remove( myLoadedFiles.lastElement() );
			}

		}

		return c;
	}

	///////////////////////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////////////////////
	@Override
	public IComponent create( String componentData )
	{
		IComponent c = null;

		// First, get the type of the component we want to load
		String type = getType( componentData );
		if( type != null )
		{
			// Get the id of the component
			UUID id = getId( componentData );

			if( id != null )
			{
				try
				{
					// Create an instance of the component with the id and data as arguments.
					Class<?> componentClass = Class.forName( type );
					if( componentClass != null )
					{
						Constructor<?> ctor = componentClass.getConstructor( UUID.class );
						c = (IComponent) ctor.newInstance( id );

						// Let the component load itself and any sub components
						if( ! c.loadComponentFromData( componentData, this, xPathReader ) )
						{
							c = null;
						}
					}
				}
				catch( ClassNotFoundException | NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e )
				{
					myLogger.severe( e.getMessage() );
				}
			}
		}

		return c;
	}

	///////////////////////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////////////////////
	private UUID getId( String componentData )
	{
		UUID id = null;

		XPath path = myXPathFac.newXPath();
		try
		{
			String theId = (String) path.evaluate( "/Component/@instanceId", new InputSource( new StringReader( componentData ) ), XPathConstants.STRING );
			if( theId != null )
			{
				id = UUID.fromString( theId );
			}
		}
		catch( XPathExpressionException e )
		{
			myLogger.severe( e.toString() );
		}

		return id;
	}

	///////////////////////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////////////////////
	private String getType( String componentData )
	{
		String type = null;

		XPath path = myXPathFac.newXPath();
		try
		{
			type = (String) path.evaluate( "/Component/Type", new InputSource( new StringReader( componentData ) ), XPathConstants.STRING );
		}
		catch( XPathExpressionException e )
		{
			myLogger.severe( e.toString() );
		}

		return type;
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
