package ruleengine.parts.Wire;

import ruleengine.parts.input.BooleanInput;
import ruleengine.parts.output.BooleanOutput;
import ruleengine.parts.IComponent;
import ruleengine.parts.data.WireDef;

public class BooleanWire extends Wire<Boolean, BooleanInput, BooleanOutput>
{
	public BooleanWire( WireDef def )
	{
		super( def );
	}

	@Override
	protected BooleanOutput getOutput( IComponent source, String name )
	{
		return source.getBooleanOutputs().get( name );
	}

	@Override
	protected BooleanInput getInput( IComponent target, String name )
	{
		return target.getBooleanInputs().get( name );
	}


}
