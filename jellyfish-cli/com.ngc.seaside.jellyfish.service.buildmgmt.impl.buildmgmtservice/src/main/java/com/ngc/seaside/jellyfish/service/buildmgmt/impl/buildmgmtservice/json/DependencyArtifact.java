package com.ngc.seaside.jellyfish.service.buildmgmt.impl.buildmgmtservice.json;

import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildDependency;

public class DependencyArtifact implements IBuildDependency {

   private String groupId;
   private String artifactId;
   private ArtifactGroup group;

   @Override
   public String getVersion() {
      return group.getVersion();
   }

   @Override
   public String getVersionPropertyName() {
      return group.getVersionPropertyName();
   }

   @Override
   public String getGroupId() {
      return groupId;
   }

   public DependencyArtifact setGroupId(String groupId) {
      this.groupId = groupId;
      return this;
   }

   @Override
   public String getArtifactId() {
      return artifactId;
   }

   public DependencyArtifact setArtifactId(String artifactId) {
      this.artifactId = artifactId;
      return this;
   }

   public ArtifactGroup getGroup() {
      return group;
   }

   public DependencyArtifact setGroup(ArtifactGroup group) {
      this.group = group;
      return this;
   }
}
