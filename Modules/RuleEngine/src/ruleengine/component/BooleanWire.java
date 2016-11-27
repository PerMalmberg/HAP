package ruleengine.component;

import ruleengine.component.composite.CompositeComponent;
import ruleengine.component.data.WireDef;

import java.util.UUID;

public class BooleanWire implements IWire
{
	private final WireDef myDef;
	private BooleanOutput output = null;
	private BooleanInput input = null;


	public BooleanWire( WireDef def )
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
			output = source.getBooleanOutputs().get( myDef.getSourceOutput() );
			input = target.getBooleanInputs().get( myDef.getTargetInput() );
			if( output != null && input != null)
			{
				output.connect( input );
				res = true;
			}
		}

		return res;
	}

	public void disconnect()
	{
		if( output != null && input != null )
		{
			output.disconnect( input );
		}
	}
}
