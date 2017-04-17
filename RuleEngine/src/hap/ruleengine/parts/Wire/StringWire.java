package hap.ruleengine.parts.Wire;

import hap.ruleengine.parts.IComponent;
import hap.ruleengine.parts.data.WireDef;
import hap.ruleengine.parts.input.StringInput;
import hap.ruleengine.parts.output.StringOutput;

import java.util.UUID;

public class StringWire extends Wire<String, StringInput, StringOutput>
{
	public StringWire( WireDef def )
	{
		super( def );
	}

	@Override
	protected StringOutput getOutput( IComponent source, UUID id )
	{
		return source.getStringOutputs().get( id );
	}

	@Override
	protected StringInput getInput( IComponent target, UUID id )
	{
		return target.getStringInputs().get( id );
	}
}
