package com.ngc.seaside.jellyfish.cli.gradle.tasks;

import com.ngc.seaside.jellyfish.cli.gradle.JellyFishProjectGenerator;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.util.HashMap;
import java.util.Map;

public class JellyFishCliCommandTask extends DefaultTask {

   private String command;

   private boolean failBuildOnException = true;

   private Map<String, String> arguments = new HashMap<>();

   @TaskAction
   public void runJellyfish() {
      new JellyFishProjectGenerator(getLogger())
            .setCommand(command)
            .setArguments(arguments)
            .setFailBuildOnException(failBuildOnException)
            .generate();
   }

   public String getCommand() {
      return command;
   }

   public void setCommand(String command) {
      this.command = command;
   }

   public Map<String, String> getArguments() {
      return arguments;
   }

   public void setArguments(Map<String, String> arguments) {
      this.arguments = arguments;
   }

   public boolean isFailBuildOnException() {
      return failBuildOnException;
   }

   public void setFailBuildOnException(boolean failBuildOnException) {
      this.failBuildOnException = failBuildOnException;
   }
}
