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
package com.ngc.seaside.jellyfish.cli.gradle.plugins;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Extension for running analysis commands for the system descriptor project, such as through Sonarqube.
 */
public class SystemDescriptorAnalysisExtension {

   public static final String EXTENSION_NAME = "sdAnalysis";

   private final Set<String> commands = new LinkedHashSet<>();
   private final Map<String, Object> args = new LinkedHashMap<>();

   /**
    * Sets the analysis commands to be run.
    * 
    * @param commands analysis commands
    */
   public void setCommands(Iterable<Object> commands) {
      this.commands.clear();
      commands(commands);
   }

   /**
    * Returns the set of analysis commands to be run.
    * 
    * @return the set of analysis commands
    */
   public Set<String> getCommands() {
      return commands;
   }

   /**
    * Adds the given analysis command to be run.
    * 
    * @param command analysis command
    */
   public void command(Object command) {
      commands(command);
   }

   /**
    * Adds the given analysis commands to be run.
    * 
    * @param commands analysis commands
    */
   public void commands(Object... commands) {
      commands(Arrays.asList(commands));
   }

   /**
    * Adds the given analysis commands to be run.
    * 
    * @param commands analysis commands
    */
   public void commands(Iterable<Object> commands) {
      for (Object command : commands) {
         this.commands.add(command.toString());
      }
   }

   /**
    * Returns the arguments to be used for the analysis.
    * 
    * @return the arguments to be used for the analysis
    */
   public Map<String, Object> getArgs() {
      return args;
   }

   /**
    * Sets the arguments to be used for the analysis.
    * 
    * @param args the arguments to be used for the analysis
    */
   public void setArgs(Map<String, Object> args) {
      args.clear();
      args(args);
   }

   /**
    * Adds an argument to be used for analysis.
    * 
    * @param key key
    * @param value value
    */
   public void arg(String key, Object value) {
      args(Collections.singletonMap(key, value));
   }

   /**
    * Adds the arguments to be used for the analysis.
    * 
    * @param args the arguments to be used for the analysis
    */
   public void args(Map<String, Object> args) {
      this.args.putAll(args);
   }

}
