// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.parts.Wire;

import hap.ruleengine.parts.IComponent;
import hap.ruleengine.parts.composite.CompositeComponent;
import hap.ruleengine.parts.data.WireDef;
import hap.ruleengine.parts.input.Input;
import hap.ruleengine.parts.output.Output;

import java.util.UUID;

public abstract class Wire<T, In extends Input<T>, Out extends Output<T>> implements IWire
{
	private final WireDef myDef;
	private Out output = null;
	private In input = null;

	Wire( WireDef def )
	{
		myDef = def;
	}

	public static <T, In extends Input<T>, Out extends Output<T>> WireDef createDef( Out output, In input, String simpleClassName )
	{
		WireDef def = new WireDef();
		def.setType( simpleClassName );
		def.setTargetInput( input.getId().toString() );
		def.setTargetComponent( input.getParent().getId().toString() );
		def.setSourceOutput( output.getId().toString() );
		def.setSourceComponent( output.getParent().getId().toString() );
		return def;
	}

	@Override
	public boolean connect( CompositeComponent cc )
	{
		boolean res = false;

		IComponent source = cc.getComponent( UUID.fromString( myDef.getSourceComponent() ) );
		IComponent target = cc.getComponent( UUID.fromString( myDef.getTargetComponent() ) );

		if( source != null && target != null
				// Make sure we're not connecting a wire between to connection points on the same component.
				&& source != target )
		{
			output = getOutput( source, UUID.fromString( myDef.getSourceOutput() ) );
			input = getInput( target,  UUID.fromString( myDef.getTargetInput() ) );
			if( output != null && input != null )
			{
				res = output.connect( input );
			}
		}

		return res;
	}

	protected abstract Out getOutput( IComponent source, UUID id );

	protected abstract In getInput( IComponent target, UUID id );

	@Override
	public void disconnect()
	{
		if( output != null && input != null )
		{
			output.disconnect( input );
		}
	}

	@Override
	public UUID getSourceComponent()
	{
		return UUID.fromString( myDef.getSourceComponent() );
	}

	@Override
	public UUID getSourceOutput()
	{
		return UUID.fromString( myDef.getSourceOutput() );
	}

	@Override
	public UUID getTargetComponent()
	{
		return UUID.fromString( myDef.getTargetComponent() );
	}

	@Override
	public UUID getTargetInput()
	{
		return UUID.fromString( myDef.getTargetInput() );
	}
}
