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
package com.ngc.seaside.jellyfish.service.buildmgmt.impl.buildmgmtservice.config;

import com.google.common.base.Preconditions;

import com.ngc.seaside.jellyfish.service.buildmgmt.api.DependencyScope;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildDependency;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Provides a simple way to configure dependencies that are referenced when projects are generated as code.  Note that
 * many values can be hardcoded as strings or injected as {@link #propertyNamed(String) properties} at runtime.
 * See {@link com.ngc.seaside.jellyfish.service.buildmgmt.impl.buildmgmtservice.DefaultDependenciesConfiguration} for an
 * example.
 */
public class DependenciesConfiguration {

   /**
    * The name of the property that will resolve the current version of Jellyfish at runtime.
    */
   public static String CURRENT_JELLYFISH_VERSION_PROPERTY_NAME = "jellyfish.version";

   /**
    * All groups in this configuration.
    */
   private final Collection<Group> groups = new ArrayList<>();

   /**
    * Gets all the groups of this configuration.
    */
   public Collection<Group> getGroups() {
      return Collections.unmodifiableCollection(groups);
   }

   /**
    * Adds a partial group that is not yet configured to this configuration and returns it for use.
    */
   public PartialGroup addGroup() {
      Group group = new Group()
            .setComplete(false);
      groups.add(group);
      return new PartialGroup(group);
   }

   /**
    * Injects the given properties into this configuration.
    */
   public DependenciesConfiguration resolve(Properties properties) {
      Preconditions.checkNotNull(properties, "properties may not be null!");
      groups.forEach(g -> g.resolve(properties));
      return this;
   }

   /**
    * Validates the configuration.
    *
    * @throws IllegalStateException if the configuration is not valid
    */
   public DependenciesConfiguration validate() {
      // Make sure all group configurations are complete.
      String message = groups.stream()
            .filter(g -> !g.isComplete())
            .map(Group::toString)
            .collect(Collectors.joining(", "));
      if (message.length() > 0) {
         throw new IllegalStateException("The following groups are not completely configured!  " + message);
      }
      return this;
   }

   public static Artifact artifact(String artifactId) {
      return new Artifact().artifactId(artifactId);
   }

   public static Artifact artifact(Property artifactId) {
      return new Artifact().artifactId(artifactId);
   }

   /**
    *
    * @param name of the property
    * @return the property with passed in name
    */
   public static Property propertyNamed(String name) {
      Preconditions.checkNotNull(name, "name may not be null!");
      Preconditions.checkArgument(!name.trim().isEmpty(), "name may not be empty!");
      return new Property(name);
   }

   public static Property currentJellyfishVersion() {
      return propertyNamed(CURRENT_JELLYFISH_VERSION_PROPERTY_NAME);
   }

   public static class Group {

      final Collection<Artifact> artifacts = new ArrayList<>();
      PropertyValue versionPropertyName;
      PropertyValue version;
      PropertyValue defaultGroupId = PropertyValue.optional();
      DependencyScope defaultScope;
      boolean complete;

      private Group() {
      }

      public Collection<Artifact> getArtifacts() {
         return Collections.unmodifiableCollection(artifacts);
      }

      public String getVersionPropertyName() {
         return versionPropertyName.getValue();
      }

      public String getVersion() {
         return version.getValue();
      }

      public String getDefaultGroupId() {
         return defaultGroupId.getValue();
      }

      /**
       *
       * @param versionPropertyName name of the version property to be created by this Property
       * @return group created by the property
       */
      public Group versionPropertyName(Property versionPropertyName) {
         Preconditions.checkNotNull(versionPropertyName, "versionPropertyName may not be null!");
         this.versionPropertyName = new PropertyValue(versionPropertyName);
         return this;
      }

      /**
       *
       * @param versionPropertyName name of the version property to be created by this String
       * @return group created b the String
       */
      public Group versionPropertyName(String versionPropertyName) {
         Preconditions.checkNotNull(versionPropertyName, "versionPropertyName may not be null!");
         Preconditions.checkArgument(!versionPropertyName.trim().isEmpty(), "versionPropertyName may not be empty!");
         this.versionPropertyName = PropertyValue.constant(versionPropertyName);
         return this;
      }

      /**
       *
       * @param version used to create the group via a property
       * @return the group created by the passed in property
       */
      public Group version(Property version) {
         Preconditions.checkNotNull(version, "version may not be null!");
         this.version = new PropertyValue(version);
         return this;
      }

      /**
       *
       * @param version used to create the group via a String
       * @return the group created by the passed in string
       */
      public Group version(String version) {
         Preconditions.checkNotNull(version, "version may not be null!");
         Preconditions.checkArgument(!version.trim().isEmpty(), "version may not be empty!");
         this.version = PropertyValue.constant(version);
         return this;
      }

      /**
       *
       * @param defaultGroupId The group id to be used as the default via a property
       * @return the group with this default group ID
       */
      public Group defaultGroupId(Property defaultGroupId) {
         Preconditions.checkNotNull(defaultGroupId, "defaultGroupId may not be null!");
         this.defaultGroupId = new PropertyValue(defaultGroupId);
         return this;
      }

      /**
       *
       * @param defaultGroupId The group id to be used as the default via a String
       * @return he group with this default group ID
       */
      public Group defaultGroupId(String defaultGroupId) {
         Preconditions.checkNotNull(defaultGroupId, "defaultGroupId may not be null!");
         Preconditions.checkArgument(!defaultGroupId.trim().isEmpty(), "defaultGroupId may not be empty!");
         this.defaultGroupId = PropertyValue.constant(defaultGroupId);
         return this;
      }

      public DependencyScope getDefaultScope() {
         return defaultScope;
      }

      public Group defaultScope(DependencyScope defaultScope) {
         this.defaultScope = Preconditions.checkNotNull(defaultScope, "defaultScope may not be null!");
         return this;
      }

      /**
       *
       * @param artifact that will be added
       * @param artifacts collection of artifacts to add
       * @return Group
       */
      public Group includes(Artifact artifact, Artifact... artifacts) {
         Preconditions.checkNotNull(artifact, "artifact may not be null!");
         Collection<Artifact> all = new ArrayList<>();
         all.add(artifact);
         all.addAll(Arrays.asList(artifacts));
         return includes(all);
      }

      /**
       *
       * @param artifacts collection of artifacts
       * @return Group
       */
      public Group includes(Collection<Artifact> artifacts) {
         Preconditions.checkNotNull(artifacts, "artifacts may not be null!");
         artifacts.forEach(a -> this.artifacts.add(a.setGroup(this)));
         return this;
      }

      boolean isComplete() {
         return complete;
      }

      Group setComplete(boolean complete) {
         this.complete = complete;
         return this;
      }

      Group resolve(Properties properties) {
         versionPropertyName.resolve(properties);
         version.resolve(properties);
         defaultGroupId.resolve(properties);
         artifacts.forEach(a -> a.resolve(properties));
         return this;
      }

      @Override
      public String toString() {
         return "Group{"
               + "artifacts=" + artifacts
               + ", versionPropertyName=" + versionPropertyName
               + ", version=" + version
               + ", defaultGroupId=" + defaultGroupId
               + ", defaultScope=" + defaultScope
               + '}';
      }
   }

   public static class PartialGroup {

      private final Group group;

      private PartialGroup(Group group) {
         this.group = group;
      }

      public PartialGroup versionPropertyName(Property versionPropertyName) {
         group.versionPropertyName(versionPropertyName);
         return this;
      }

      public PartialGroup versionPropertyName(String versionPropertyName) {
         group.versionPropertyName(versionPropertyName);
         return this;
      }

      public Group version(Property version) {
         return group.version(version).setComplete(true);
      }

      public Group version(String version) {
         return group.version(version).setComplete(true);
      }
   }

   public static class Artifact implements IBuildDependency {

      Group group;
      PropertyValue artifactId;
      PropertyValue groupId = PropertyValue.optional();
      DependencyScope scope;

      @Override
      public String getArtifactId() {
         return artifactId.getValue();
      }

      @Override
      public String getGroupId() {
         return Preconditions.checkNotNull(groupId.hasValue() ? groupId.getValue() : group.getDefaultGroupId(),
                                           "artifact %s has no group ID and its group has no default group ID!",
                                           getArtifactId());
      }

      @Override
      public String getVersion() {
         return group.getVersion();
      }

      @Override
      public String getVersionPropertyName() {
         return group.getVersionPropertyName();
      }

      /**
       *
       * @return DependencyScope
       */
      public DependencyScope getScope() {
         return Preconditions.checkNotNull(scope != null ? scope : group.getDefaultScope(),
                                           "artifact %s has no scope and its group has no default scope!",
                                           getArtifactId());
      }

      /**
       *
       * @param artifactId Property
       * @return the Artifact ID
       */
      public Artifact artifactId(Property artifactId) {
         Preconditions.checkNotNull(artifactId, "artifactId may not be null!");
         this.artifactId = new PropertyValue(artifactId);
         return this;
      }

      /**
       *
       * @param artifactId String to get the Artifact ID
       * @return the Artifact ID
       */
      public Artifact artifactId(String artifactId) {
         Preconditions.checkNotNull(artifactId, "artifactId may not be null!");
         Preconditions.checkArgument(!artifactId.trim().isEmpty(), "artifactId may not be empty!");
         this.artifactId = PropertyValue.constant(artifactId);
         return this;
      }

      /**
       *
       * @param groupId  Property used to get the group ID artifact
       * @return The group ID artifact
       */
      public Artifact groupId(Property groupId) {
         Preconditions.checkNotNull(groupId, "groupId may not be null!");
         this.groupId = new PropertyValue(groupId);
         return this;
      }

      /**
       *
       * @param groupId String used to get the group ID artifact
       * @return The group ID artifact
       */
      public Artifact groupId(String groupId) {
         Preconditions.checkNotNull(groupId, "groupId may not be null!");
         Preconditions.checkArgument(!groupId.trim().isEmpty(), "groupId may not be empty!");
         this.groupId = PropertyValue.constant(groupId);
         return this;
      }

      public Artifact scope(DependencyScope scope) {
         this.scope = Preconditions.checkNotNull(scope, "scope may not be null!");
         return this;
      }

      Artifact setGroup(Group group) {
         this.group = group;
         return this;
      }

      Artifact resolve(Properties properties) {
         artifactId.resolve(properties);
         groupId.resolve(properties);
         return this;
      }

      @Override
      public String toString() {
         return "Artifact{"
               + ", artifactId=" + artifactId
               + ", groupId=" + groupId
               + ", scope=" + scope
               + '}';
      }
   }

   public static class Property {

      private final String name;

      private Property(String name) {
         this.name = name;
      }

      public String getName() {
         return name;
      }

      @Override
      public String toString() {
         return "Property{"
               + "name='" + name + '\''
               + '}';
      }
   }

   private static class PropertyValue {

      private final Property property;
      private String value;

      private PropertyValue(Property property) {
         this.property = property;
      }

      private static PropertyValue constant(String value) {
         PropertyValue p = new PropertyValue(CONSTANT_PROPERTY);
         p.value = value;
         return p;
      }

      private static PropertyValue optional() {
         PropertyValue p = new PropertyValue(OPTIONAL_PROPERTY);
         p.value = null;
         return p;
      }

      public Property getProperty() {
         return property;
      }

      public String getValue() {
         if (value == null && property != OPTIONAL_PROPERTY && property != CONSTANT_PROPERTY) {
            throw new IllegalStateException(String.format("no value for property %s has been configured!",
                                                          property.getName()));
         }
         return value;
      }

      public boolean hasValue() {
         return property == CONSTANT_PROPERTY || value != null;
      }

      public void resolve(Properties properties) {
         if (property != CONSTANT_PROPERTY && property != OPTIONAL_PROPERTY) {
            value = properties.getProperty(property.getName());
         }
      }

      @Override
      public String toString() {
         return "PropertyValue{"
               + "property=" + property
               + ", value='" + value + '\''
               + '}';
      }
   }

   private static final Property CONSTANT_PROPERTY = new Property(null);
   private static final Property OPTIONAL_PROPERTY = new Property(null);
}
