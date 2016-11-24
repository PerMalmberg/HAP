// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package ruleengine.component;

public interface IWire
{
	String getName();
	IOutput getOutput();
	IInput getInput();
}
