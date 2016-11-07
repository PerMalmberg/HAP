# HAP
Home Automation Platform

This is a very early work-in-progress, don't expect things to work.

## Requirements
* Java-based

###Central process
* Runs on low-end hardware, such as the Raspberry Pi.
* Central process
  * No reload needed to update configuraton.
  * Runs as daemon/service.
  * CLI tool to control modules. (Central CLI, or ccli for short)
    * Provides access to the commands exposed by the central process
    * Provides access to the commands exposed by each of the modules.
    * If possible, provide bash-like editing ability, otherwise allow a command to be automatically repeated so that statuses can be printed in a polled fashion.
    
###Modules
* Each module is a separate process started by the central server, this enables adding/removing/starting/stopping modules at run-time.
* Each module process is started with a low initial memory footprint specified by the Java Xms-swich, eg. -Xms20m, to prevent unnecessary large memory usage, at the cost of longer startup-times.
* Each module is self-contained, i.e. no shared class files. This enables individual updates and prevents versioning problems between modules.
* Modules are controlled (start/stop/load/unload/commands) via a central process.  
* Modules must support automatic reload of configurations.
* Each module instance receives a user-defined indentifier from the central process at startup which is used to identify that instance.
* Modules expose a command API used from the ccli.
  * The API is specified via JSON; command name, arguments and what parameters they accept.
  * When a module is loaded, the command API is parsed and the resulting parser is reused over and over for that instance.
* Modules communicates to external parties via MQTT with the topic scheme ```/<ModuleType>/<UserDefinedID>/<topic>``` and data is in JSON format.
  

##External dependencies
MQTT broker


## Misc links
https://github.com/Pi4J/pi4j
