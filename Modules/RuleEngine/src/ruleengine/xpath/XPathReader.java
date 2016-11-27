// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package ruleengine.xpath;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class XPathReader implements IXPathReader
{
	private final XPathFactory myXPathFac = XPathFactory.newInstance();

	@Override
	public List<String> getTextValues( String path, String startNode )
	{
		List<String> values = new ArrayList<>();

		XPath xPath = myXPathFac.newXPath();
		try
		{
			NodeList map = (NodeList) xPath.evaluate( path, new InputSource( new StringReader( startNode ) ), XPathConstants.NODESET );
			for( int i = 0; i < map.getLength(); ++ i )
			{
				Node item = map.item( i );
				NamedNodeMap attr = item.getAttributes();
				Node src = attr.getNamedItem( "src" );
				if( src != null )
				{
					values.add( src.getTextContent() );
				}


			}
		}
		catch( XPathExpressionException e )
		{
			e.printStackTrace();
		}

		return values;
	}

	@Override
	public NodeList getSubNodes( String path, String startNode )
	{
		NodeList nodes = null;

		XPath xPath = myXPathFac.newXPath();

		try {
			nodes = (NodeList)xPath.evaluate( path, new InputSource( new StringReader( startNode ) ), XPathConstants.NODESET );
		}
		catch( XPathExpressionException e )
		{
			e.printStackTrace();
		}

		return nodes;
	}


}
