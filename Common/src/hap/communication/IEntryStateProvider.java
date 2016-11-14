// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.


package hap.communication;

import hap.communication.state.CommState;

public interface IEntryStateProvider {
	CommState createEntryState(Communicator com );
}