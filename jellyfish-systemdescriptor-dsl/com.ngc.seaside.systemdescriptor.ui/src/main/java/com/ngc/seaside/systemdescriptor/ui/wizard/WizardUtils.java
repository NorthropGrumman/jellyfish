/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE: All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.systemdescriptor.ui.wizard;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class WizardUtils {

   /**
    * The name of the property in $JELLYFISH_USER_HOME/jellyfish.properties for identifying the default
    * header file to be used.
    */
   public static final String JELLYFISH_USER_HOME_HEADER_PROPERTY = "jellyfish.generated.header";

   public static final String JELLYFISH_USER_HOME_GRADLE_URL_PROPERTY = "jellyfish.generated.gradle.url";

   public static final String JELLYFISH_USER_HOME_ENVIRONMENT_VARIABLE = "JELLYFISH_USER_HOME";
   public static final String DEFAULT_JELLYFISH_USER_HOME_FOLDER_NAME = ".jellyfish";
   public static final String JELLYFISH_PROPERTIES_FILE_NAME = "jellyfish.properties";
   public static final String GRADLE_USER_HOME_ENVIRONMENT_VARIABLE = "GRADLE_USER_HOME";

   public static final String DEFAULT_NEXUS_CONSOLIDATED = "http://10.207.42.137/nexus/repository/maven-public";
   public static final String DEFAULT_GRADLE_DISTRIBUTION = "4.9";

   /**
    * Returns the {@link FileHeader}
    */
   public static FileHeader getFileHeader() {
      Map<String, String> jellyfishProperties = getJellyfishProperties();
      String headerFile = jellyfishProperties.get(JELLYFISH_USER_HOME_HEADER_PROPERTY);
      if (headerFile == null) {
         return FileHeader.DEFAULT_HEADER;
      }
      return new FileHeader(Paths.get(headerFile));
   }

   /**
    * Returns the url to the remote gradle distribution.
    * 
    * @return the url to the remote gradle distribution
    */
   public static String getGradleDistributionUrl() {
      Map<String, String> jellyfishProperties = getJellyfishProperties();
      String url = jellyfishProperties.get(JELLYFISH_USER_HOME_GRADLE_URL_PROPERTY);
      if (url == null) {
         return getDefaultGradleDistributionUrl();
      }
      return url;
   }

   private static String getDefaultGradleDistributionUrl() {
      String gradleUserHome = System.getProperty("GRADLE_USER_HOME_ENVIRONMENT_VARIABLE");
      if (gradleUserHome == null) {
         gradleUserHome = System.getenv(GRADLE_USER_HOME_ENVIRONMENT_VARIABLE);
         if (gradleUserHome == null) {
            gradleUserHome = System.getProperty("user.home") + File.separatorChar + ".gradle";
         }
      }
      Path gradleProperties = Paths.get(gradleUserHome, "gradle.properties");
      String nexusConsolidated = null;
      if (Files.isRegularFile(gradleProperties)) {
         Properties properties = new Properties();
         try {
            properties.load(Files.newBufferedReader(gradleProperties));
         } catch (IOException e) {
            // ignore
         }
         nexusConsolidated = properties.getProperty("nexusConsolidated");
      }
      if (nexusConsolidated == null) {
         nexusConsolidated = DEFAULT_NEXUS_CONSOLIDATED;
      }
      return nexusConsolidated + "/gradle/gradle/" + DEFAULT_GRADLE_DISTRIBUTION + "/gradle-"
               + DEFAULT_GRADLE_DISTRIBUTION + "-bin.zip";
   }

   private static Map<String, String> getJellyfishProperties() {
      Map<String, String> jellyfishProperties = new HashMap<>();
      String jellyfishUserHomeValue = System.getProperty(JELLYFISH_USER_HOME_ENVIRONMENT_VARIABLE);
      if (jellyfishUserHomeValue == null) {
         jellyfishUserHomeValue = System.getenv(JELLYFISH_USER_HOME_ENVIRONMENT_VARIABLE);
         if (jellyfishUserHomeValue == null) {
            jellyfishUserHomeValue = System.getProperty("user.home") + File.separatorChar
                     + DEFAULT_JELLYFISH_USER_HOME_FOLDER_NAME;
         }
      }
      if (jellyfishUserHomeValue.startsWith("\"")) {
         jellyfishUserHomeValue = jellyfishUserHomeValue.substring(1);
      }
      if (jellyfishUserHomeValue.endsWith("\"")) {
         jellyfishUserHomeValue = jellyfishUserHomeValue.substring(0, jellyfishUserHomeValue.length() - 1);
      }
      Path jellyfishUserHome = Paths.get(jellyfishUserHomeValue);
      Path propertiesFile = jellyfishUserHome.resolve(JELLYFISH_PROPERTIES_FILE_NAME);
      if (Files.isRegularFile(propertiesFile)) {
         Properties properties = new Properties();
         try {
            properties.load(Files.newBufferedReader(propertiesFile));
         } catch (IOException e) {
            // ignore
         }
         for (String property : properties.stringPropertyNames()) {
            jellyfishProperties.put(property, properties.getProperty(property));
         }

      }
      return jellyfishProperties;
   }
}
