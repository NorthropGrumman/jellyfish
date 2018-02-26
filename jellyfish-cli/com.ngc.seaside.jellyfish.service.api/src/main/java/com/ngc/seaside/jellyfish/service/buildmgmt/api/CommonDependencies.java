package com.ngc.seaside.jellyfish.service.buildmgmt.api;

/**
 * Contains group and artifact IDs for commonly referenced dependencies.
 */
public enum CommonDependencies {

   PROTOBUF_GRADLE_PLUGIN("com.google.protobuf", "protobuf-gradle-plugin"),
   OSGI_CORE("org.osgi", "osgi.core"),
   OSGI_ENTERPRISE("org.osgi", "osgi.enterprise"),
   GAUVA("com.google.guava", "guava"),
   BLOCS_API("com.ngc.blocs", "api"),
   BLOCS_SERVICE_API("com.ngc.blocs", "service.api"),
   JUNIT("junit", "junit"),
   MOCKITO_CORE("org.mockito", "mockito-core"),
   SEASIDE_SERVICE_API("com.ngc.seaside", "service.api"),
   SEASIDE_TRANSPORT_SERVICE_API("com.ngc.seaside", "service.transport.api");

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
