/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.jellyfish.service.name.project.impl;

import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;

import java.util.Objects;

/**
 * A simple implementation of {@code IProjectInformation}.
 */
public class ProjectInformation implements IProjectInformation {

   private String groupId;
   private String artifactId;
   private String versionPropertyName;
   private boolean generated;
   private String generatedDirectoryName;

   @Override
   public String getGavFormattedString() {
      return String.format("%s:%s:$%s", groupId, artifactId, versionPropertyName);
   }

   @Override
   public String getDirectoryName() {
      return String.format("%s%s.%s",
                           generated ? generatedDirectoryName + "/" : "",
                           groupId,
                           artifactId);
   }

   @Override
   public String getGroupId() {
      return groupId;
   }

   public ProjectInformation setGroupId(String groupId) {
      this.groupId = groupId;
      return this;
   }

   @Override
   public String getArtifactId() {
      return artifactId;
   }

   public ProjectInformation setArtifactId(String artifactId) {
      this.artifactId = artifactId;
      return this;
   }

   @Override
   public String getVersionPropertyName() {
      return versionPropertyName;
   }

   public ProjectInformation setVersionPropertyName(String versionPropertyName) {
      this.versionPropertyName = versionPropertyName;
      return this;
   }

   public boolean isGenerated() {
      return generated;
   }

   public ProjectInformation setGenerated(boolean generated) {
      this.generated = generated;
      return this;
   }

   public String getGeneratedDirectoryName() {
      return generatedDirectoryName;
   }

   public ProjectInformation setGeneratedDirectoryName(String generatedDirectoryName) {
      this.generatedDirectoryName = generatedDirectoryName;
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (!(o instanceof ProjectInformation)) {
         return false;
      }
      ProjectInformation that = (ProjectInformation) o;
      return Objects.equals(groupId, that.groupId)
            && Objects.equals(artifactId, that.artifactId)
            && Objects.equals(versionPropertyName, that.versionPropertyName)
            && Objects.equals(generatedDirectoryName, that.generatedDirectoryName)
            && generated == that.generated;
   }

   @Override
   public int hashCode() {
      return Objects.hash(groupId,
                          artifactId,
                          versionPropertyName,
                          generated,
                          generatedDirectoryName);
   }
}
