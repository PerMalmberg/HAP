// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package ruleengine.component.composite;

import ruleengine.component.*;
import ruleengine.component.data.ComponentDef;
import ruleengine.component.data.CompositeDef;
import ruleengine.component.data.CompositeDef.Imports.Import;
import ruleengine.component.data.WireDef;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CompositeComponent extends Component
{
	private final List<BooleanWire> myWire = new ArrayList<>();

	public CompositeComponent()
	{
		super( UUID.randomUUID() );
		myData = new CompositeDef();
	}

	public CompositeComponent( UUID id )
	{
		super( id );
	}

	private CompositeDef myData;
	private final HashMap<UUID, IComponent> myComponent = new HashMap<>();

	@Override
	public int getSubComponentCount()
	{
		return myComponent.size();
	}

	@Override
	public void setup( CompositeComponent cc )
	{

	}

	@Override
	public void inputChanged( BooleanInput input )
	{

	}

	///////////////////////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////////////////////
	public boolean loadCompositeFromData( CompositeDef data, IComponentFactory factory )
	{
		myData = data;

		boolean res = loadNativeComponents( factory );
		res = res && loadCompositeComponents( data, factory );
		res = res && connectWires( data );

		return res;
	}

	///////////////////////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////////////////////
	private boolean connectWires( CompositeDef data )
	{
		boolean res = true;

		List<WireDef> wires = data.getWires().getWireDef();
		for( int i = 0; res && i < wires.size(); ++ i )
		{
			WireDef wire = wires.get( i );
			if( "boolean".equals( wire.getType() ) )
			{
				BooleanWire w = new BooleanWire( wire );
				res = w.connect( this );
				myWire.add( w );
			}
			else
			{
				// TODO: Other wire types
			}
		}

		if( ! res )
		{
			myWire.forEach( BooleanWire::disconnect );
		}

		return res;
	}

	///////////////////////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////////////////////
	private boolean loadCompositeComponents( CompositeDef data, IComponentFactory factory )
	{
		boolean res = true;

		List<CompositeDef.Imports.Import> imports = data.getImports().getImport();
		for( int i = 0; res && i < imports.size(); ++ i )
		{
			Import imp = imports.get( i );
			CompositeComponent cc = factory.create( new File( imp.getSrc() ) );
			if( cc == null )
			{
				res = false;
			}
			else
			{
				myComponent.put( UUID.fromString( imp.getInstanceId() ), cc );
			}
		}

		return res;
	}

	///////////////////////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////////////////////
	private boolean loadNativeComponents( IComponentFactory factory )
	{
		boolean res = true;

		List<ComponentDef> definitions = myData.getComponents().getComponentDef();

		for( int i = 0; res && i < definitions.size(); ++ i )
		{
			ComponentDef def = definitions.get( i );

			IComponent comp = factory.create( def, this );
			if( comp != null )
			{
				myComponent.put( comp.getId(), comp );
			}
			else
			{
				res = false;
			}
		}

		return res;
	}

	///////////////////////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////////////////////
	public IComponent getComponent( UUID uuid )
	{
		return myComponent.get( uuid );
	}
}
