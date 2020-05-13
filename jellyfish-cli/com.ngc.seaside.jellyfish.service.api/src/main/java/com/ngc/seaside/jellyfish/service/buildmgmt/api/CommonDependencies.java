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
