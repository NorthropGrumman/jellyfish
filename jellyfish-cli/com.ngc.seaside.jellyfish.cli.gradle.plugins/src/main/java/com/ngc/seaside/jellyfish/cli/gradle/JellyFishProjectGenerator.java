package com.ngc.seaside.jellyfish.cli.gradle;

import org.gradle.api.GradleException;
import org.gradle.api.logging.Logger;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class JellyFishProjectGenerator {

   private final Logger logger;

   private String command;

   private boolean failBuildOnException = true;

   private Map<String, String> arguments = new HashMap<>();

   private Supplier<Boolean> executionCondition = () -> true;

   public JellyFishProjectGenerator(Logger logger) {
      this.logger = logger;
   }

   /**
    * generates a jellyfish project
    */
   public void generate() {
      if (executionCondition.get()) {

         String previousProperty = System.getProperty("NG_FW_HOME");
         boolean isPropertySet = previousProperty != null && previousProperty.trim().equals("");
         if (!isPropertySet) {
            System.setProperty("NG_FW_HOME", Paths.get(System.getProperty("user.dir")).toAbsolutePath().toString());
         }
         try {
            doGenerate();
         } finally {
            if (!isPropertySet) {
               System.clearProperty("NG_FW_HOME");
            }
         }

      }
   }

   public String getCommand() {
      return command;
   }

   public JellyFishProjectGenerator setCommand(String command) {
      this.command = command;
      return this;
   }

   public boolean isFailBuildOnException() {
      return failBuildOnException;
   }

   public JellyFishProjectGenerator setFailBuildOnException(boolean failBuildOnException) {
      this.failBuildOnException = failBuildOnException;
      return this;
   }

   public Map<String, String> getArguments() {
      return arguments;
   }

   public JellyFishProjectGenerator setArguments(Map<String, String> arguments) {
      this.arguments = arguments;
      return this;
   }

   public Supplier<Boolean> getExecutionCondition() {
      return executionCondition;
   }

   public JellyFishProjectGenerator setExecutionCondition(Supplier<Boolean> executionCondition) {
      this.executionCondition = executionCondition;
      return this;
   }

   private void doGenerate() {
      if (command == null || command.trim().equals("")) {
         throw new GradleException("command must be set!");
      }
      List<String> stringArgs = arguments.entrySet()
            .stream()
            .map(e -> String.format("-D%s=%s", e.getKey(), e.getValue()))
            .collect(Collectors.toList());
      stringArgs.add(0, command);

      try {
         logger.debug("Running JellyFish command " + command + ".");
         GradleJellyFishRunner.run(stringArgs.toArray(new String[stringArgs.size()]));
         logger.debug("JellyFish command " + command + " executed successfully.");
      } catch (Throwable t) {
         if (failBuildOnException) {
            throw new GradleException("Jellyfish command " + command + " failed!", t);
         } else {
            logger.error("JellyFish command " + command + " failed!", t);
         }
      }
   }
}
