package com.ngc.seaside.jellyfish.cli.gradle.plugins

import com.ngc.seaside.jellyfish.JellyFish
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.internal.tasks.options.Option

class JellyFishCliCommandTask extends DefaultTask {
	// The command to execute, it is the same command used when running the CLI (ie, jellyfish validate)
    def command
    
    // Points to the location of the System Descriptor source files. The default is "src/main/sd" for the current project
    def inputDir
    
    // Specify the arguments to be passed to the command. Use as --args arg1=val1,arg2=val2.
    // Args set in the task are treated as default args and will all be discarded if the args option is used on the command line.
    @Option(option = "args",
            description = "Arguments to be passed to the command. Separate multiple args with commas. i.e. arg1=val1,arg2=val2",
            order = 1)
    def args
	
    @TaskAction
    def doCommand() {        
    	def jellyfishRunArgs = []
    	 
    	if (command != null) {
    		jellyfishRunArgs.add(command)
    	}
    	
    	if (inputDir != null) {
    		jellyfishRunArgs.add("-DinputDir=" + inputDir)
    	}
    	
    	if (args != null) {
    		args.split(',').each { eachArg -> jellyfishRunArgs.add("-D" + eachArg) }
    	}
    	
        JellyFish.run((String[]) jellyfishRunArgs.toArray())
    }
}
