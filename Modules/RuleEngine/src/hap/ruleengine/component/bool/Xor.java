// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.component.bool;

import hap.ruleengine.parts.Component;
import hap.ruleengine.parts.composite.CompositeComponent;
import hap.ruleengine.parts.input.BooleanInput;
import hap.ruleengine.parts.output.BooleanOutput;

import java.util.UUID;

public class Xor extends Component
{
	private BooleanInput inA;
	private BooleanInput inB;
	private BooleanOutput out;

	public Xor( UUID id, boolean executionState )
	{
		super( id, executionState );
	}

	@Override
	public void setup( CompositeComponent cc )
	{
		inA = new BooleanInput( "A", UUID.fromString( "87cd2526-c95a-44a6-a7cd-8d94e97bd924" ), this );
		inB = new BooleanInput( "B", UUID.fromString( "d549c1a6-6624-4ada-a33f-b09918b43e57" ), this );
		out = new BooleanOutput( "Out", UUID.fromString( "6f48e6ab-a186-40c1-880e-2fbf2d5375c6" ), this );
		addInput( inA );
		addInput( inB );
		addOutput( out );
	}

	@Override
	public void inputChanged( BooleanInput input )
	{
		out.set( inA.getValue() ^ inB.getValue() );
	}
}
