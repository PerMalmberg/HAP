// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine

import cmdparser4j.*
import hap.LogFormatter
import hap.SysUtil
import hap.ruleengine.parts.composite.CompositeComponent
import hap.ruleengine.state.BaseState
import hap.ruleengine.state.LoadRules
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import java.util.HashMap
import java.util.UUID
import java.util.logging.*

class RuleEngine private constructor() : chainedfsm.FSM<BaseState>()
{
	private val loadedSchema = HashMap<UUID, CompositeComponent>()
	private val myResult = SystemOutputParseResult()
	private val myParser = CmdParser4J(myResult)
	private val myCfg: XMLConfigurationReader = XMLConfigurationReader(myResult)
	private var myLog: Logger = Logger.getLogger("RuleEngine")
	private var myWorkDir: Path = Paths.get("")
	private var myIsTerminated = false


	private fun initialize(args: Array<String>): Boolean
	{
		myParser.accept("--config").asString(1).describedAs("Full path to configuration file").withAlias("-c")
		myParser.accept("--working-dir").asString(1).describedAs("The working directory").withAlias("-w")
		myParser.accept("--log-to-console").asBoolean(1).describedAs("If specified, logging to console will be enabled").withAlias("-l")
		myParser.accept("--log-to-file").asBoolean(1).describedAs("If specified, logging to file will be enabled").withAlias("-f")
		myParser.accept("--log-level").asString(1).describedAs("Specifies the log level").withAlias("-ll")
		myParser.accept("--help").asSingleBoolean().describedAs("Print help text").withAlias("-?").setHelpCommand()

		myCfg.setMatcher("--log-to-console", XMLConfigurationReader.NodeMatcher("HAP/Module/Logging", "console"))
		myCfg.setMatcher("--log-to-file", XMLConfigurationReader.NodeMatcher("HAP/Module/Logging", "file"))
		myCfg.setMatcher("--log-level", XMLConfigurationReader.NodeMatcher("HAP/Module/Logging/Level"))

		return setup(myParser, myResult, *args)
	}

	fun setup(parser: CmdParser4J, result: IParseResult, vararg args: String): Boolean
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
				val usage = SystemOutputUsageFormatter("RuleEngine")
				parser.getUsage(usage)
				myLog.info(usage.toString())
				res = false
			}
			else
			{
				myWorkDir = Paths.get(SysUtil.getFullOrRelativePath(RuleEngine::class.java, myParser.getString("--working-dir", 0, "data")))

				if (Files.exists(myWorkDir))
				{
					if (myParser.getBool("--log-to-file"))
					{
						try
						{
							val fh = FileHandler(Paths.get(myWorkDir.toString(), "RuleEngine.log").toString(), 1024 * 1024, 10, true)
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
			}

			if (!parser.getBool("--log-to-console", 0, true))
			{
				myLog.removeHandler(console)
			}
		}
		else
		{
			myLog.info(result.parseResult)
		}

		return res
	}

	private fun run(): Boolean
	{
		try
		{
			setState(LoadRules(loadedSchema, myParser, this))

			while (!myIsTerminated)
			{
				Thread.sleep(0, 1)
				currentState.tick()
			}
		}
		catch (e: InterruptedException)
		{
			myLog.finest(e.message)
		}

		return myIsTerminated
	}

	companion object
	{
		@JvmStatic fun main(args: Array<String>)
		{
			val m = RuleEngine()

			val result = if (m.initialize(args) && m.run()) 0 else 1

			System.exit(result)
		}
	}

}
