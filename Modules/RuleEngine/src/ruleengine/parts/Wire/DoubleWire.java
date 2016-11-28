package ruleengine.parts.Wire;

import ruleengine.parts.input.DoubleInput;
import ruleengine.parts.output.DoubleOutput;
import ruleengine.parts.IComponent;
import ruleengine.parts.data.WireDef;

public class DoubleWire extends Wire<Double, DoubleInput, DoubleOutput>
{
	public DoubleWire( WireDef def )
	{
		super( def );
	}

	@Override
	protected DoubleOutput getOutput( IComponent source, String name )
	{
		return source.getDoubleOutputs().get( name );
	}

	@Override
	protected DoubleInput getInput( IComponent target, String name )
	{
		return target.getDoubleInputs().get( name );
	}


}
