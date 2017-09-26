package com.ngc.seaside.jellyfish.cli.gradle.tasks;

import com.ngc.seaside.jellyfish.JellyFish;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.util.Map;

public class JellyFishCliCommandTask extends DefaultTask {

   private String command;

   private String inputDir;

   private boolean failBuildOnException = true;

   private Map<String, String> arguments;

   @TaskAction
   public void doCommand() {
      if (command == null || command.trim().equals("")) {
         throw new GradleException("command must be set!");
      }
      if (inputDir == null) {
         inputDir = new File(".").getAbsolutePath();
      }

      arguments.put("inputDir", inputDir);
      String[] stringArgs = arguments.entrySet()
            .stream()
            .map(e -> String.format("-D%s=%s", e.getKey(), e.getValue()))
            .toArray(String[]::new);

      try {
         JellyFish.run(stringArgs);
         getLogger().debug("Jellyfish command " + command + " executed successfully.");
      } catch (Throwable t) {
         if (failBuildOnException) {
            throw new GradleException("Jellyfish command " + command + " failed!", t);
         } else {
            getLogger().error("Jellyfish command " + command + " failed!", t);
         }
      }
   }

   public String getCommand() {
      return command;
   }

   public void setCommand(String command) {
      this.command = command;
   }

   public String getInputDir() {
      return inputDir;
   }

   public void setInputDir(String inputDir) {
      this.inputDir = inputDir;
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
