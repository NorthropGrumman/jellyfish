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
package com.ngc.seaside.jellyfish.cli.gradle;

import com.ngc.seaside.jellyfish.Jellyfish;

import org.gradle.api.GradleException;
import org.gradle.api.logging.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * A utility to run an instance of Jellyfish within Gradle.
 */
public class JellyFishProjectGenerator {

   private final Logger logger;

   private String command;

   private boolean failBuildOnException = true;

   private Map<String, String> arguments = new HashMap<>();

   private Supplier<Boolean> executionCondition = () -> true;

   /**
    * Creates a new generator.
    */
   public JellyFishProjectGenerator(Logger logger) {
      this.logger = logger;
   }

   /**
    * generates a jellyfish project
    */
   public void generate() {
      if (executionCondition.get()) {
         try {
            logger.debug("Running JellyFish command " + command + ".");
            // Avoid issues when calling this from Gradle.  If we don't do this we can get exceptions like
            // GStringImpl cannot be cast to java.lang.String
            Jellyfish.getService().run(command,
                                       asPureJavaTypes(arguments),
                                       Collections.singleton(new GradleJellyfishModule()));
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

   private static Map<String, String> asPureJavaTypes(Map<?, ?> map) {
      Map<String, String> pure = new HashMap<>();
      for (Map.Entry<?, ?> entry : map.entrySet()) {
         Object key = entry.getKey();
         Object value = entry.getValue();
         if (key != null && value != null) {
            pure.put(key.toString(), value.toString());
         }
      }
      return pure;
   }
}
