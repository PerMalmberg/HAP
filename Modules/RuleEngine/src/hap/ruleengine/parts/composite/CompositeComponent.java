// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.parts.composite;

import hap.ruleengine.parts.Component;
import hap.ruleengine.parts.IComponent;
import hap.ruleengine.parts.IComponentFactory;
import hap.ruleengine.parts.Wire.BooleanWire;
import hap.ruleengine.parts.Wire.DoubleWire;
import hap.ruleengine.parts.Wire.IWire;
import hap.ruleengine.parts.Wire.StringWire;
import hap.ruleengine.parts.data.ComponentDef;
import hap.ruleengine.parts.data.CompositeDef;
import hap.ruleengine.parts.data.CompositeDef.Imports.Import;
import hap.ruleengine.parts.data.WireDef;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CompositeComponent extends Component
{
	private final List<IWire> myWire = new ArrayList<>();
	private final String mySourceFile;

	public CompositeComponent()
	{
		super( UUID.randomUUID() );
		myData = new CompositeDef();
		mySourceFile = null;
	}

	public CompositeComponent( UUID id, String sourceFile )
	{
		super( id );
		mySourceFile = sourceFile;
	}

	private CompositeDef myData;
	private final HashMap<UUID, IComponent> myComponent = new HashMap<>();

	@Override
	public int getSubComponentCount()
	{
		return myComponent.size();
	}

	@Override
	public void store( CompositeDef data )
	{
		// A composite component stores itself (as an Import)
		Import imp = new Import();
		imp.setName( getName() );
		imp.setInstanceId( getId().toString() );
		imp.setSrc( mySourceFile );
		data.getImports().getImport().add( imp );
	}

	@Override
	public void setup( CompositeComponent cc )
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
			IWire w = null;
			if( BooleanWire.class.getSimpleName().equals( wire.getType() ) )
			{
				w = new BooleanWire( wire );
			}
			else if( DoubleWire.class.getSimpleName().equals( wire.getType() ) )
			{
				w = new DoubleWire( wire );
			}
			else if( StringWire.class.getSimpleName().equals( wire.getType() ) )
			{
				w = new StringWire( wire );
			}
			else
			{
				res = false;
			}

			if( w != null )
			{
				res = w.connect( this );
				myWire.add( w );
			}
		}

		if( ! res )
		{
			myWire.forEach( IWire::disconnect );
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
			CompositeComponent cc = factory.create( new File( imp.getSrc() ), UUID.fromString( imp.getInstanceId() ) );
			if( cc == null )
			{
				res = false;
			}
			else
			{
				cc.setName( imp.getName() );
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
				addComponent( comp );
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

	///////////////////////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////////////////////
	public void serialize( CompositeDef data )
	{
		for( UUID uid : myComponent.keySet() )
		{
			IComponent comp = myComponent.get( uid );
			comp.store( data );
		}
	}

	public void addComponent( IComponent c )
	{
		myComponent.put( c.getId(), c );
	}
}
