// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.parts.Wire;

import hap.ruleengine.parts.IComponent;
import hap.ruleengine.parts.input.Input;
import hap.ruleengine.parts.output.Output;
import hap.ruleengine.parts.composite.CompositeComponent;
import hap.ruleengine.parts.data.WireDef;

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

	@Override
	public boolean connect( CompositeComponent cc )
	{
		boolean res = false;

		IComponent source = cc.getComponent( UUID.fromString( myDef.getSourceComponent() ) );
		IComponent target = cc.getComponent( UUID.fromString( myDef.getTargetComponent() ) );

		if( source != null && target != null )
		{
			output = getOutput( source, myDef.getSourceOutput() );
			input = getInput( target, myDef.getTargetInput() );
			if( output != null && input != null )
			{
				output.connect( input );
				res = true;
			}
		}

		return res;
	}

	protected abstract Out getOutput( IComponent source, String name );

	protected abstract In getInput( IComponent target, String name );

	@Override
	public void disconnect()
	{
		if( output != null && input != null )
		{
			output.disconnect( input );
		}
	}

}
