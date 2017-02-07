// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap.ruleengine.state

import chainedfsm.EnterChain
import chainedfsm.LeaveChain
import chainedfsm.interfaces.IEnter
import chainedfsm.interfaces.ILeave
import cmdparser4j.CmdParser4J
import hap.SysUtil
import hap.communication.Communicator
import hap.ruleengine.config.HAP
import hap.ruleengine.parts.ComponentFactory
import hap.ruleengine.parts.composite.CompositeComponent
import java.io.File
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import javax.xml.XMLConstants
import javax.xml.bind.JAXBContext
import javax.xml.validation.SchemaFactory

class LoadRules(val loadedSchema: HashMap<UUID, CompositeComponent>, val parser: CmdParser4J, com: Communicator) : BaseState(com), IEnter, ILeave
{
	private var xsdPath = Files.createTempDirectory("RuleEngine-tmp")
	private val factory = ComponentFactory(ComponentFactory.STANDARD_LIBRARY)

	init
	{
		EnterChain<LoadRules>(this, this)
		LeaveChain<LoadRules>(this, this)
	}

	override fun enter()
	{
		val jc = JAXBContext.newInstance(HAP::class.java)
		val u = jc.createUnmarshaller()

		val xsd: URL
		if (SysUtil.isRunningFromJAR(javaClass))
		{
			// Extract XSDs from JAR to temporary directory
			SysUtil.extractFilesFromJar(javaClass, xsdPath, { ze -> ze.name.startsWith("config") && ze.name.endsWith("xsd") })
			xsd = Paths.get(xsdPath.toString(), "/config/Config.xsd").toUri().toURL()
			SysUtil.deleteFolderOnExit(xsdPath)
		}
		else
		{
			xsd = javaClass.getResource("/config/Config.xsd")
		}

		val schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
		val f = File(xsd.toURI())
		val schema = schemaFactory.newSchema(f)
		u.schema = schema

		val configFile = parser.getString("--config")


		val config = u.unmarshal(File(configFile)) as HAP

		for (c in config.composites.composite)
		{
			val uuid = UUID.randomUUID()
			val composite = factory.create( factory.findImport( c.src), uuid)
			if (composite != null)
			{
				loadedSchema.put(uuid, composite)
			}
		}
	}

	override fun leave()
	{
		SysUtil.deleteRecursively(xsdPath.toFile())
	}

}
