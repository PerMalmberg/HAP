// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.parts;

import hap.ruleengine.parts.composite.CompositeComponent;
import hap.ruleengine.parts.data.CompositeDef;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;

public class CompositeSerializer
{
	public boolean serialize( CompositeComponent cc, File target )
	{
		boolean res = true;

		CompositeDef def = new CompositeDef();
		def.setComponents( new CompositeDef.Components() );
		def.setImports( new CompositeDef.Imports() );
		def.setWires( new CompositeDef.Wires() );
		cc.serialize( def );


		try
		{
			JAXBContext jc = JAXBContext.newInstance( CompositeDef.class );
			Marshaller m = jc.createMarshaller();
			m.marshal( def, target );
		}
		catch( JAXBException e )
		{
			res = false;
		}

		return res;
	}
}
