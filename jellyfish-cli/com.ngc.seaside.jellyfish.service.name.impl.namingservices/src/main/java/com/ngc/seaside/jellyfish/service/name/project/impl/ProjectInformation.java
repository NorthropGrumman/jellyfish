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
