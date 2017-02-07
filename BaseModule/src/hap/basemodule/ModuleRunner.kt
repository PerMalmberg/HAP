// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.basemodule

import cmdparser4j.*
import hap.LogFormatter
import hap.SysUtil
import hap.communication.Communicator
import hap.communication.IModuleRunner
import hap.message.Message

import java.io.IOException
import java.net.URI
import java.net.URISyntaxException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.logging.*

abstract class ModuleRunner(private val mqttClientId: String) : IModuleRunner
{
	private val myResult: IParseResult = SystemOutputParseResult()
	private val myParser = CmdParser4J(myResult)
	private val myCfg: XMLConfigurationReader = XMLConfigurationReader(myResult)
	private var myCom: Communicator? = null
	protected var myLog: Logger = Logger.getLogger(mqttClientId)
	private var myBroker: String? = null
	protected var myWorkDir: Path = Paths.get("")
	private var myIsTerminated = false



	fun initialize(args: Array<String>): Boolean
	{
		myParser.accept("--broker").asString(1).describedAs("The DNS or IP of the MQTT broker").setMandatory().withAlias("-b")
		myParser.accept("--topic").asString(1).describedAs("The root topic used by the HAP Core").setMandatory().withAlias("-r")
		myParser.accept("--config").asString(1).describedAs("Full path to configuration file").setMandatory().withAlias("-c")
		myParser.accept("--working-dir").asString(1).describedAs("The working directory").withAlias("-w")
		myParser.accept("--log-to-console").asBoolean(1).describedAs("If specified, logging to console will be enabled").withAlias("-l")
		myParser.accept("--log-to-file").asBoolean(1).describedAs("If specified, logging to file will be enabled").withAlias("-f")
		myParser.accept("--log-level").asString(1).describedAs("Specifies the log level").withAlias("-ll")
		myParser.accept("--help").asSingleBoolean().describedAs("Print help text").withAlias("-?").setHelpCommand()

		myCfg.setMatcher("--log-to-console", XMLConfigurationReader.NodeMatcher("HAP/Module/Logging", "console"))
		myCfg.setMatcher("--log-to-file", XMLConfigurationReader.NodeMatcher("HAP/Module/Logging", "file"))
		myCfg.setMatcher("--log-level", XMLConfigurationReader.NodeMatcher("HAP/Module/Logging/Level"))

		initCmdParser(myParser, myCfg)

		val res = setup(myParser, myResult, *args)
		if (res)
		{
			myCom = Communicator(myBroker, mqttClientId, myLog)
		}

		return res
	}

	// Overridden by the module and used to read command line arguments.
	protected abstract fun initializeModule(myParser: CmdParser4J): Boolean

	// Overridden by the module and used to setup additional command line arguments.
	protected abstract fun initCmdParser(parser: CmdParser4J, configurationReader: XMLConfigurationReader)

	private fun setup(parser: CmdParser4J, result: IParseResult, vararg args: String): Boolean
	{
		// Disable default global logging handlers
		LogManager.getLogManager().reset()

		for (h in Logger.getLogger("").handlers)
		{
			Logger.getGlobal().removeHandler(h)
		}

		var res = parser.parse("--config", myCfg, *args)

		val console = ConsoleHandler()
		console.formatter = LogFormatter()
		myLog.level = Level.INFO
		console.level = Level.INFO
		myLog.addHandler(console)

		if (res)
		{
			val lvl = myParser.getString("--log-level", 0, "info")
			val level = Level.parse(lvl.toUpperCase())
			myLog.level = level
			console.level = level

			if (parser.getBool("--help"))
			{
				val usage = SystemOutputUsageFormatter(mqttClientId)
				parser.getUsage(usage)
				myLog.info(usage.toString())
				res = false
			}
			else
			{
				myWorkDir = Paths.get(SysUtil.getFullOrRelativePath(ModuleRunner::class.java, myParser.getString("--working-dir", 0, "data")))
				myBroker = parser.getString("--broker")

				if (Files.exists(myWorkDir))
				{
					if (myParser.getBool("--log-to-file"))
					{
						try
						{
							val fh = FileHandler(Paths.get(myWorkDir.toString(), mqttClientId + ".log").toString(), 1024 * 1024, 10, true)
							fh.formatter = LogFormatter()
							fh.level = level
							myLog.addHandler(fh)
						}
						catch (e: IOException)
						{
							res = false
							myLog.severe(e.message)
						}

					}
				}
				else
				{
					myLog.severe("Working directory does not exist (" + myWorkDir.toString() + ")")
					res = false
				}

				try
				{
					val uri = URI(parser.getString("--broker"))
					if (uri.scheme == null)
					{
						myLog.severe("Broker must be specified with URI scheme, i.e. tcp://<DNS|IP>")
						res = false
					}
				}
				catch (e: URISyntaxException)
				{
					myLog.severe("Invalid broker specified")
					res = false
				}

			}

			if (!parser.getBool("--log-to-console"))
			{
				myLog.removeHandler(console)
			}
		}
		else
		{
			myLog.info(result.parseResult)
		}


		Message.setTopicRoot(myParser.getString("--topic"))

		if (res)
		{
			// Let the module initialize
			res = initializeModule(myParser)
		}

		return res
	}

	fun run(stateProvider: IModuleRunner): Boolean
	{

		myCom!!.start(stateProvider)

		try
		{
			while (!myIsTerminated)
			{
				Thread.sleep(0, 1)
				myCom!!.tick()
			}
		}
		catch (e: InterruptedException)
		{
			myLog.finest(e.message)
		}

		return myIsTerminated
	}

	// Call this to terminate the module
	override fun terminate()
	{
		myIsTerminated = true
	}


}
