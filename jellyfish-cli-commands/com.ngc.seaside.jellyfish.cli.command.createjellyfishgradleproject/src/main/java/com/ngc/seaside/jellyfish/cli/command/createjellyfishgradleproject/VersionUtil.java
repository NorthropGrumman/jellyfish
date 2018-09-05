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
package com.ngc.seaside.jellyfish.cli.command.createjellyfishgradleproject;

import com.google.common.base.Preconditions;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class VersionUtil {

   private static final String CONFIG_PROPERTIES_FILE =
         "com.ngc.seaside.jellyfish.cli.command.createjellyfishgradleproject.config.properties";
   private static final String VERSION_PROPERTY = "version";

   private VersionUtil() {
   }

   /**
    *
    * @return The JellyFish version
    */
   public static String getCurrentJellyfishVersion() {
      String version;
      try (InputStream is = VersionUtil.class.getClassLoader().getResourceAsStream(CONFIG_PROPERTIES_FILE)) {
         Properties props = new Properties();
         props.load(is);
         version = props.getProperty(VERSION_PROPERTY);
         Preconditions.checkState(version != null, "version not set in configuration properties!");
      } catch (IOException e) {
         throw new IllegalStateException("failed to load configuration properties from classpath!", e);
      }
      return version;
   }
}
