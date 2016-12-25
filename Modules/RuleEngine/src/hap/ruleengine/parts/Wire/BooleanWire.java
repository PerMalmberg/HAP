package hap.ruleengine.parts.Wire;

import hap.ruleengine.parts.input.BooleanInput;
import hap.ruleengine.parts.output.BooleanOutput;
import hap.ruleengine.parts.IComponent;
import hap.ruleengine.parts.data.WireDef;

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
