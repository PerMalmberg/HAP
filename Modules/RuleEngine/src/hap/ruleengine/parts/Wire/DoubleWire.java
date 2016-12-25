package hap.ruleengine.parts.Wire;

import hap.ruleengine.parts.input.DoubleInput;
import hap.ruleengine.parts.output.DoubleOutput;
import hap.ruleengine.parts.IComponent;
import hap.ruleengine.parts.data.WireDef;

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
