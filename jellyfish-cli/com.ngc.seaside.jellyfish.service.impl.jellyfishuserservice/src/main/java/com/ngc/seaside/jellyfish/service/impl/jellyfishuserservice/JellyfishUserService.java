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
package com.ngc.seaside.jellyfish.service.impl.jellyfishuserservice;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;

import com.ngc.seaside.jellyfish.service.user.api.IJellyfishUserService;
import com.ngc.seaside.systemdescriptor.service.log.api.ILogService;

public class JellyfishUserService implements IJellyfishUserService {

   public static final String JELLYFISH_USER_HOME_ENVIRONMENT_VARIABLE = "JELLYFISH_USER_HOME";
   public static final String DEFAULT_JELLYFISH_USER_HOME_FOLDER_NAME = ".jellyfish";
   public static final String DEFAULT_PLUGINS_FOLDER_NAME = "plugins";
   public static final String JELLYFISH_PROPERTIES_FILE_NAME = "jellyfish.properties";

   private final Path jellyfishUserHome;
   private final Path jellyfishPlugins;
   private final Map<String, String> properties;

   @Inject
   public JellyfishUserService(ILogService logService) {
      String jellyfishUserHomeValue = System.getProperty(JELLYFISH_USER_HOME_ENVIRONMENT_VARIABLE);
      if (jellyfishUserHomeValue == null) {
         jellyfishUserHomeValue = System.getenv(JELLYFISH_USER_HOME_ENVIRONMENT_VARIABLE);
         if (jellyfishUserHomeValue == null) {
            String userHome = System.getProperty("user.home");
            jellyfishUserHomeValue = userHome + File.separatorChar + DEFAULT_JELLYFISH_USER_HOME_FOLDER_NAME;
         }
      }
      if (jellyfishUserHomeValue.startsWith("\"")) {
         jellyfishUserHomeValue = jellyfishUserHomeValue.substring(1);
      }
      if (jellyfishUserHomeValue.endsWith("\"")) {
         jellyfishUserHomeValue = jellyfishUserHomeValue.substring(0, jellyfishUserHomeValue.length() - 1);
      }
      jellyfishUserHome = Paths.get(jellyfishUserHomeValue);
      if (!Files.exists(jellyfishUserHome)) {
         try {
            Files.createDirectories(jellyfishUserHome);
         } catch (IOException e) {
            throw new UncheckedIOException("Unable to create JELLYFISH_USER_HOME folder " + jellyfishUserHome, e);
         }
      }
      logService.debug(JellyfishUserService.class, "Setting JELLYFISH_USER_HOME to " + jellyfishUserHome);
      jellyfishPlugins = jellyfishUserHome.resolve(DEFAULT_PLUGINS_FOLDER_NAME);
      if (!Files.exists(jellyfishPlugins)) {
         try {
            Files.createDirectories(jellyfishPlugins);
         } catch (IOException e) {
            throw new UncheckedIOException("Unable to create plugins folder " + jellyfishPlugins, e);
         }
      }
      Properties properties = new Properties();
      Path propertiesFile = jellyfishUserHome.resolve(JELLYFISH_PROPERTIES_FILE_NAME);
      if (Files.isRegularFile(propertiesFile)) {
         try {
            properties.load(Files.newBufferedReader(propertiesFile));
         } catch (IOException e) {
            throw new UncheckedIOException("Unable to parse properties file " + propertiesFile, e);
         }
      }
      Map<String, String> jellyfishProperties = new LinkedHashMap<>();
      for (String property : properties.stringPropertyNames()) {
         jellyfishProperties.put(property, properties.getProperty(property));
      }
      this.properties = Collections.unmodifiableMap(jellyfishProperties);
   }

   @Override
   public Map<String, String> getJellyfishUserProperties() {
      return properties;
   }

   @Override
   public Path getJellyfishUserHome() {
      return jellyfishUserHome;
   }

   @Override
   public Path getPluginsDirectory() {
      return jellyfishPlugins;
   }

}
