package hap.ruleengine.parts.Wire;

import hap.ruleengine.parts.input.BooleanInput;
import hap.ruleengine.parts.output.BooleanOutput;
import hap.ruleengine.parts.IComponent;
import hap.ruleengine.parts.data.WireDef;

import java.util.UUID;

public class BooleanWire extends Wire<Boolean, BooleanInput, BooleanOutput>
{
	public BooleanWire( WireDef def )
	{
		super( def );
	}

	@Override
	protected BooleanOutput getOutput( IComponent source, UUID id )
	{
		return source.getBooleanOutputs().get( id );
	}

	@Override
	protected BooleanInput getInput( IComponent target, UUID id )
	{
		return target.getBooleanInputs().get( id );
	}


}
