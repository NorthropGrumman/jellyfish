package com.ngc.seaside.jellyfish.cli.gradle.plugins

import com.ngc.seaside.jellyfish.JellyFish
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class JellyFishCliCommandTask extends DefaultTask {
    def command
    def inputDir

    @TaskAction
    def doCommand() {        
    	def args = []
    	 
    	if (command != null) {
    		args.add(command)
    	}
    	
    	if (inputDir != null) {
    		args.add("-DinputDir=" + inputDir)
    	}
    	
        JellyFish.run((String[]) args.toArray())
    }
}