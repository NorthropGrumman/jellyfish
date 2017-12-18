package com.ngc.seaside.jellyfish.cli.gradle.tasks;

import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.cli.gradle.GradleJellyFishRunner;

import org.gradle.api.GradleException;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JellyFishCliCommandTask extends AbstractJellyFishCliCommandTask {

   private String command;

   private String inputDir;

   private boolean failBuildOnException = true;

   private Map<String, String> arguments = new HashMap<>();

   @Override
   protected void doExecuteTask() {
      if (command == null || command.trim().equals("")) {
         throw new GradleException("command must be set!");
      }
      if (inputDir == null) {
         inputDir = new File(".").getAbsolutePath();
      }

      arguments.put(CommonParameters.INPUT_DIRECTORY.getName(), inputDir);
      List<String> stringArgs = arguments.entrySet()
            .stream()
            .map(e -> String.format("-D%s=%s", e.getKey(), e.getValue()))
            .collect(Collectors.toList());
      stringArgs.add(0, command);

      try {
         getLogger().debug("Running JellyFish command " + command + ".");
         GradleJellyFishRunner.run(stringArgs.toArray(new String[stringArgs.size()]));
         getLogger().debug("JellyFish command " + command + " executed successfully.");
      } catch (Throwable t) {
         if (failBuildOnException) {
            throw new GradleException("Jellyfish command " + command + " failed!", t);
         } else {
            getLogger().error("JellyFish command " + command + " failed!", t);
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
