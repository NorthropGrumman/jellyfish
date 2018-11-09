/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.jellyfish.cli.gradle.tasks;

import com.ngc.seaside.jellyfish.cli.gradle.JellyFishProjectGenerator;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.util.HashMap;
import java.util.Map;

/**
 * A Gradle task that runs Jellyfish.
 */
public class JellyFishCliCommandTask extends DefaultTask {

   private String command;

   private boolean failBuildOnException = true;

   private Map<String, String> arguments = new HashMap<>();

   /**
    * Runs Jellyfish.
    */
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

   public void argument(String key, String value) {
      this.arguments.put(key, value);
   }

   public void setArguments(Map<String, String> arguments) {
      this.arguments = new HashMap<>(arguments);
   }

   public boolean isFailBuildOnException() {
      return failBuildOnException;
   }

   public void setFailBuildOnException(boolean failBuildOnException) {
      this.failBuildOnException = failBuildOnException;
   }
}
