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
package com.ngc.seaside.jellyfish.service.buildmgmt.api;

/**
 * Contains group and artifact IDs for commonly referenced dependencies.
 */
public enum CommonDependencies {

   PROTOBUF_GRADLE_PLUGIN("com.google.protobuf", "protobuf-gradle-plugin"),
   PROTOBUF_JAVA("com.google.protobuf", "protobuf-java"),
   SPARK_CORE("com.sparkjava", "spark-core"),
   OSGI_CORE("org.osgi", "osgi.core"),
   OSGI_ENTERPRISE("org.osgi", "osgi.enterprise"),
   GAUVA("com.google.guava", "guava"),
   BLOCS_API("com.ngc.blocs", "api"),
   BLOCS_SERVICE_API("com.ngc.blocs", "service.api"),
   BLOCS_GRADLE_PLUGINS("com.ngc.blocs", "gradle.plugin"),
   JUNIT("junit", "junit"),
   MOCKITO_CORE("org.mockito", "mockito-core"),
   SEASIDE_SERVICE_API("com.ngc.seaside", "service.api"),
   SEASIDE_TRANSPORT_SERVICE_API("com.ngc.seaside", "service.transport.api"),
   SEASIDE_GRADLE_PLUGINS("com.ngc.seaside", "gradle.plugins"),
   JELLYFISH_GRADLE_PLUGINS("com.ngc.seaside", "jellyfish.cli.gradle.plugins"),
   SONARQUBE_GRADLE_PLUGIN("org.sonarsource.scanner.gradle", "sonarqube-gradle-plugin");

   private final String gropuId;
   private final String artifactId;

   CommonDependencies(String gropuId, String artifactId) {
      this.gropuId = gropuId;
      this.artifactId = artifactId;
   }

   /**
    * Gets the group ID of the dependency.
    */
   public String getGropuId() {
      return gropuId;
   }

   /**
    * Gets the artifact ID of the dependency.
    */
   public String getArtifactId() {
      return artifactId;
   }
}
