package com.ngc.seaside.jellyfish.service.buildmgmt.api;

public interface IBuildDependency {

   String getGroupId();

   String getArtifactId();

   String getVersion();

   String getVersionPropertyName();

}
