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
package com.ngc.seaside.jellyfish.service.impl.jellyfishuserservice;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.service.user.api.IJellyfishUserService;

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
