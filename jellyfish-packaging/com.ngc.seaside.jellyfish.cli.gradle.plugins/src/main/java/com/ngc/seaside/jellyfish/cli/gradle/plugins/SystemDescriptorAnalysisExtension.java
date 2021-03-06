/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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

   /**
    * The name of the extension that can be referenced in the build script.
    */
   public static final String EXTENSION_NAME = "sdAnalysis";

   private final Set<String> reports = new LinkedHashSet<>();
   private final Set<String> commands = new LinkedHashSet<>();
   private final Map<String, Object> args = new LinkedHashMap<>();

   /**
    * Returns the set of analysis commands to be run.
    *
    * @return the set of analysis commands
    */
   public Set<String> getCommands() {
      return commands;
   }

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
    * Returns the set of reports to generate.
    *
    * @return the set of reports
    */
   public Set<String> getReports() {
      return reports;
   }

   /**
    * Sets the reports to generate.  Reports are only generated when running with Gradle, not when running with
    * Sonarqube.
    *
    * @param reports the reports to generate
    */
   public void setReports(Iterable<Object> reports) {
      this.reports.clear();
      reports(reports);
   }

   /**
    * Adds the given report to the reports to generate.  Reports are only generated when running with Gradle, not when
    * running with Sonarqube.
    *
    * @param report the report to generate
    */
   public void report(Object report) {
      reports(report);
   }

   /**
    * Adds the given reports to generate.  Reports are only generated when running with Gradle, not when running with
    * Sonarqube.
    *
    * @param reports the reports to generate
    */
   public void reports(Object... reports) {
      reports(Arrays.asList(reports));
   }

   /**
    * Adds the given reports to generate.  Reports are only generated when running with Gradle, not when running with
    * Sonarqube.
    *
    * @param reports the reports to generate
    */
   public void reports(Iterable<Object> reports) {
      for (Object report : reports) {
         this.reports.add(report.toString());
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
    * @param key   key
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
