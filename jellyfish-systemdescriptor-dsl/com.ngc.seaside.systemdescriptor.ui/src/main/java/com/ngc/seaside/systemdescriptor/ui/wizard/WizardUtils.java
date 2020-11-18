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
   public static final String JELLYFISH_USER_HOME_HEADER_PROPERTY = "jellyfish.generated.header.file";

   /**
    * The name of the property in $JELLYFISH_USER_HOME/jellyfish.properties for identifying the default
    * Gradle distribution URL to use in generated Gradle projects.
    */
   public static final String JELLYFISH_USER_HOME_GRADLE_URL_PROPERTY = "jellyfish.generated.gradle.url";

   public static final String JELLYFISH_USER_HOME_ENVIRONMENT_VARIABLE = "JELLYFISH_USER_HOME";
   public static final String DEFAULT_JELLYFISH_USER_HOME_FOLDER_NAME = ".jellyfish";
   public static final String JELLYFISH_PROPERTIES_FILE_NAME = "jellyfish.properties";
   public static final String GRADLE_USER_HOME_ENVIRONMENT_VARIABLE = "GRADLE_USER_HOME";

   public static final String DEFAULT_GRADLE_DISTRIBUTION = 
            "https\\://services.gradle.org/distributions/gradle-4.9-bin.zip";

   /**
    * Returns the {@link FileHeader}
    */
   public static FileHeader getFileHeader() {
      Map<String, String> jellyfishProperties = getJellyfishProperties();
      String headerFile = jellyfishProperties.get(JELLYFISH_USER_HOME_HEADER_PROPERTY);
      if (headerFile == null) {
         return FileHeader.DEFAULT_HEADER;
      }
      Path header = Paths.get(headerFile);
      if (!Files.isRegularFile(header)) {
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
         return DEFAULT_GRADLE_DISTRIBUTION;
      }
      return url;
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
