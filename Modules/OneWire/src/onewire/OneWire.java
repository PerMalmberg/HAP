// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package onewire;

import cmdparser4j.CmdParser4J;
import cmdparser4j.XMLConfigurationReader;
import cmdparser4j.limits.NumericLimit;
import hap.basemodule.ModuleRunner;
import hap.communication.Communicator;
import hap.communication.state.CommState;
import jowshell.Discovery;
import jowshell.logging.ILogger;
import jowshell.system.ICommandExecution;
import jowshell.system.IExecute;
import jowshell.system.ShellExecute;
import onewire.state.DiscoverNetwork;

public class OneWire extends ModuleRunner implements ILogger, ICommandExecution {

	public static void main(String... args) {
		OneWire m = new OneWire();

		int result = 1;

		if (m.initialize(args)) {
			result = m.run(m) ? 0 : 1;
		}

		System.exit(result);
	}

	public OneWire() {
		super(OneWire.class.getName());
		myExec = new ShellExecute(this);
	}


	@Override
	protected boolean initializeModule(CmdParser4J myParser) {
		// Read command line parameters and initialize module
		String myOwHost = myParser.getString("--ow-host");
		myOwTopic = myParser.getString("--ow-topic");
		myDiscovery = new Discovery(myOwHost, this, this);
		myExec.setTimeout( myParser.getInteger("--ow-timeout", 0, 5) * 1000);

		return true;
	}

	@Override
	protected void initCmdParser(CmdParser4J parser, XMLConfigurationReader configurationReader) {
		// Add more command line arguments here if needed by the module
		parser.accept("--ow-host").asString(1).setMandatory().describedAs("The host on which ow-Server is running");
		parser.accept("--ow-topic").asString(1).setMandatory().describedAs("The root MQTT-topic to which data will be published");
		parser.accept("--ow-timeout").asInteger(1, new NumericLimit<>(1, 10)).describedAs("Timeout for 1-Wire commands, in seconds").withAlias("-t");
		configurationReader.setMatcher("--ow-host", new XMLConfigurationReader.NodeMatcher("/OneWire/Settings/OWServer/Host"));
		configurationReader.setMatcher("--ow-timeout", new XMLConfigurationReader.NodeMatcher("/OneWire/Settings/OWServer/Timeout"));
		configurationReader.setMatcher("--ow-topic", new XMLConfigurationReader.NodeMatcher("/OneWire/Settings/MQTT/PublishTopic"));
	}

	@Override
	public CommState createEntryState(Communicator com) {
		return new DiscoverNetwork(com, this);
	}

	private String myOwTopic = null;
	private Discovery myDiscovery;
	private final ShellExecute myExec;

	public String getOwTopic()
	{
		return myOwTopic;
	}

	public Discovery getDiscovery()
	{
		return myDiscovery;
	}

	@Override
	public void debug(String msg) {
		myLog.finest(msg);
	}

	@Override
	public void error(String msg) {
		myLog.severe(msg);
	}

	@Override
	public void error(Exception ex) {
		myLog.severe(ex.getMessage());
	}

	@Override
	public String getOwRead() {
		return "owread";
	}

	@Override
	public String getOwDir() {
		return "owdir";
	}

	@Override
	public String getOwWrite() {
		return "owwrite";
	}

	@Override
	public IExecute getExec() {
		return myExec;
	}


}
