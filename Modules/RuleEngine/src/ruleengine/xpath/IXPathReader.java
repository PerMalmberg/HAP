// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package ruleengine.xpath;

import org.w3c.dom.NodeList;

import java.util.List;

public interface IXPathReader
{
	List<String> getTextValues( String path, String startNode );

	NodeList getSubNodes( String path, String startNode );
}
