// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.onewire.state;

import chainedfsm.EnterChain;
import hap.communication.Communicator;
import hap.onewire.OneWire;


public class DiscoverNetwork extends OwBaseState
{

public DiscoverNetwork( Communicator com, OneWire oneWire )
{
	super( com, oneWire );
	new EnterChain<>( this, this::enter );
}

public void enter()
{
	if( myOw.getDiscovery().discoverTree() )
	{
		myCom.getLogger().info( "Found " + myOw.getDiscovery().getNetwork().getAllDevices().keySet().size() + " devices" );
		myCom.setState( new ReadValueState( myCom, myOw ) );
	} else
	{
		myOw.terminate();
	}
}
}
