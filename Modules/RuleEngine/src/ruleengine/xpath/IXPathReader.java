// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package ruleengine.xpath;

import java.util.List;

public interface IXPathReader
{
	List<String> getTextValues( String path, String startNode );
}
