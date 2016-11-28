package ruleengine.parts.Wire;

import ruleengine.parts.IComponent;
import ruleengine.parts.input.StringInput;
import ruleengine.parts.output.StringOutput;
import ruleengine.parts.data.WireDef;

public class StringWire extends Wire<String, StringInput, StringOutput>
{
	public StringWire( WireDef def )
	{
		super( def );
	}

	@Override
	protected StringOutput getOutput( IComponent source, String name )
	{
		return source.getStringOutputs().get( name );
	}

	@Override
	protected StringInput getInput( IComponent target, String name )
	{
		return target.getStringInputs().get( name );
	}
}
