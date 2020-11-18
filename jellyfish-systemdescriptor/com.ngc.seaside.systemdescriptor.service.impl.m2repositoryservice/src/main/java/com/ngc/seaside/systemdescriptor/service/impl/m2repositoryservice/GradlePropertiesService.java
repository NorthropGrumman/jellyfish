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
package com.ngc.seaside.systemdescriptor.service.impl.m2repositoryservice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Properties;

import com.ngc.seaside.systemdescriptor.service.log.api.ILogService;

public class GradlePropertiesService {

   static final String GRADLE_USER_HOME = "GRADLE_USER_HOME";
   private static final String USER_HOME_PROPERTY_NAME = "user.home";
   private static final String GRADLE_PROPERTIES_FILENAME = "gradle.properties";

   private ILogService logService;

   private Properties properties;

   /**
    * Activates this component.
    */
   public void activate() {
      properties = new Properties();

      // Load properties from GRADLE_USER_HOME, or if it's not set, from ~/.gradle/gradle.properties
      String gradleUserHome = System.getProperty(GRADLE_USER_HOME, System.getenv(GRADLE_USER_HOME));
      if (gradleUserHome == null) {
         String userHome = System.getProperty(USER_HOME_PROPERTY_NAME);
         if (userHome != null) {
            load(Paths.get(userHome, ".gradle", GRADLE_PROPERTIES_FILENAME));
         }
      } else {
         load(Paths.get(gradleUserHome, GRADLE_PROPERTIES_FILENAME));
      }

      // Load properties for ./gradle.properties; override previously-set values
      load(Paths.get(GRADLE_PROPERTIES_FILENAME));
   }

   /**
    * Deactivates this component.
    */
   public void deactivate() {
      if (properties != null) {
         properties.clear();
      }
   }

   /**
    * Sets the log service this component will use.
    *
    * @param ref the log service this component will use
    */
   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

   /**
    * Removes the log service this component will use.
    *
    * @param ref the log service this component will use
    */
   public void removeLogService(ILogService ref) {
      setLogService(null);
   }

   /**
    * Returns the property associated with the given key. The rules for resolving the key to a property are in the
    * following order:
    *
    * <ol>
    * <li>From {@link System#getProperty(String)}</li>
    * <li>From {@value #GRADLE_PROPERTIES_FILENAME} located in the current working directory</li>
    * <li>From {@value #GRADLE_PROPERTIES_FILENAME} located from the property {@value #GRADLE_USER_HOME}
    * <li>From {@value #GRADLE_PROPERTIES_FILENAME} located in <{@value #USER_HOME_PROPERTY_NAME}>/.gradle if
    * {@value #GRADLE_USER_HOME} is not set</li>
    * <li>From {@link System#getenv(String)}</li>
    * </ol>
    *
    * @param key property key
    * @return the property associated with the given key
    */
   public Optional<String> getProperty(String key) {
      String value = System.getProperty(key);
      if (value == null) {
         value = properties.getProperty(key);
         if (value == null) {
            value = System.getenv(key);
         }
      }
      return Optional.ofNullable(value);
   }

   private boolean load(Path path) {
      if (path != null && Files.isRegularFile(path)) {
         try {
            properties = new Properties(properties);
            properties.load(Files.newBufferedReader(path));
            return true;
         } catch (IOException e) {
            logService.warn(GradlePropertiesService.class, "Unable to load " + path, e);
         }
      }
      return false;
   }

}
