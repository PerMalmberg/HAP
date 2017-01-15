package hap.ruleengine.parts.Wire;

import hap.ruleengine.parts.input.DoubleInput;
import hap.ruleengine.parts.output.DoubleOutput;
import hap.ruleengine.parts.IComponent;
import hap.ruleengine.parts.data.WireDef;

import java.util.UUID;

public class DoubleWire extends Wire<Double, DoubleInput, DoubleOutput>
{
	public DoubleWire( WireDef def )
	{
		super( def );
	}

	@Override
	protected DoubleOutput getOutput( IComponent source, UUID id )
	{
		return source.getDoubleOutputs().get( id );
	}

	@Override
	protected DoubleInput getInput( IComponent target, UUID id )
	{
		return target.getDoubleInputs().get( id );
	}


}
