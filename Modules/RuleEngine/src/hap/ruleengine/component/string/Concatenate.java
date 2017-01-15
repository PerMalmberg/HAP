// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.component.string;

import hap.ruleengine.parts.Component;
import hap.ruleengine.parts.input.StringInput;
import hap.ruleengine.parts.output.StringOutput;
import hap.ruleengine.parts.composite.CompositeComponent;

import java.util.UUID;


public class Concatenate extends Component
{
	private StringInput inA;
	private StringInput inB;
	private StringOutput out;

	public Concatenate( UUID id, boolean executionState )
	{
		super( id, executionState );
	}

	@Override
	public void setup( CompositeComponent cc )
	{
		inA = new StringInput( "A", UUID.fromString( "4bf0d092-7195-4fd2-9540-c1978164fe50"),  this );
		inB = new StringInput( "B", UUID.fromString( "36abe6cb-97f1-462e-88e5-4538cea29041" ), this );
		out = new StringOutput( "Out", UUID.fromString( "90d5fe91-8938-46da-b155-711bc10511fb" ), this );
		addInput( inA );
		addInput( inB );
		addOutput( out );
	}

	@Override
	public void inputChanged( StringInput input )
	{
		if( inA.getValue() != null && inB.getValue() != null )
		{
			out.set( inA.getValue().concat( inB.getValue() ) );
		}
	}
}
