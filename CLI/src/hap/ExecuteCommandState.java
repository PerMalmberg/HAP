// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap;

import hap.communication.Communicator;
import hap.communication.state.CommState;


public class ExecuteCommandState extends CommState {
	public ExecuteCommandState(Communicator com) {
		super(com);
	}
}
