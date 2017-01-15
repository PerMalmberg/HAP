// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.component.numeric;

import hap.ruleengine.parts.Component;
import hap.ruleengine.parts.input.DoubleInput;
import hap.ruleengine.parts.output.DoubleOutput;
import hap.ruleengine.parts.composite.CompositeComponent;

import java.util.UUID;


public class Add extends Component
{
	private DoubleInput inA;
	private DoubleInput inB;
	private DoubleOutput out;

	public Add( UUID id, boolean executionState )
	{
		super( id, executionState );
	}

	@Override
	public void setup( CompositeComponent cc )
	{
		inA = new DoubleInput( "A", UUID.fromString( "62d2c8a5-8204-47d4-8263-eea5461a6225" ), this );
		inB = new DoubleInput( "B", UUID.fromString( "5c935a3a-42ca-412b-86b8-4c5a9e9a3e6b" ), this );
		out = new DoubleOutput( "Out", UUID.fromString( "83e071e4-0d9c-4da9-8ba4-1a4ccbe257d1" ), this );
		addInput( inA );
		addInput( inB );
		addOutput( out );
	}

	@Override
	public void inputChanged( DoubleInput input )
	{
		if( !Double.isNaN( inA.getValue() ) && !Double.isNaN( inB.getValue() ) )
		{
			out.set( inA.getValue() + inB.getValue() );
		}
	}
}
