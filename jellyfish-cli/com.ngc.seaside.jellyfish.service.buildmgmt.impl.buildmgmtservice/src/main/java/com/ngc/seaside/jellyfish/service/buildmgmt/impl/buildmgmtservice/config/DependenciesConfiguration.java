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

public class DependenciesConfiguration {

   public static String CURRENT_JELLYFISH_VERSION_PROPERTY_NAME = "jellyfish.version";

   private final Collection<Group> groups = new ArrayList<>();

   public Collection<Group> getGroups() {
      return Collections.unmodifiableCollection(groups);
   }

   public PartialGroup addGroup() {
      Group group = new Group()
            .setComplete(false);
      groups.add(group);
      return new PartialGroup(group);
   }

   public DependenciesConfiguration resolve(Properties properties) {
      Preconditions.checkNotNull(properties, "properties may not be null!");
      groups.forEach(g -> g.resolve(properties));
      return this;
   }

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

      public Group usingVersionPropertyNamed(Property versionPropertyName) {
         Preconditions.checkNotNull(versionPropertyName, "versionPropertyName may not be null!");
         this.versionPropertyName = new PropertyValue(versionPropertyName);
         return this;
      }

      public Group usingVersionPropertyNamed(String versionPropertyName) {
         Preconditions.checkNotNull(versionPropertyName, "versionPropertyName may not be null!");
         Preconditions.checkArgument(!versionPropertyName.trim().isEmpty(), "versionPropertyName may not be empty!");
         this.versionPropertyName = PropertyValue.constant(versionPropertyName);
         return this;
      }

      public Group atVersion(Property version) {
         Preconditions.checkNotNull(version, "version may not be null!");
         this.version = new PropertyValue(version);
         return this;
      }

      public Group atVersion(String version) {
         Preconditions.checkNotNull(version, "version may not be null!");
         Preconditions.checkArgument(!version.trim().isEmpty(), "version may not be empty!");
         this.version = PropertyValue.constant(version);
         return this;
      }

      public Group withDefaultGroupId(Property defaultGroupId) {
         Preconditions.checkNotNull(defaultGroupId, "defaultGroupId may not be null!");
         this.defaultGroupId = new PropertyValue(defaultGroupId);
         return this;
      }

      public Group withDefaultGroupId(String defaultGroupId) {
         Preconditions.checkNotNull(defaultGroupId, "defaultGroupId may not be null!");
         Preconditions.checkArgument(!defaultGroupId.trim().isEmpty(), "defaultGroupId may not be empty!");
         this.defaultGroupId = PropertyValue.constant(defaultGroupId);
         return this;
      }

      public DependencyScope getDefaultScope() {
         return defaultScope;
      }

      public Group withDefaultScope(DependencyScope defaultScope) {
         this.defaultScope = Preconditions.checkNotNull(defaultScope, "defaultScope may not be null!");
         return this;
      }

      public Group includes(Artifact artifact, Artifact... artifacts) {
         Preconditions.checkNotNull(artifact, "artifact may not be null!");
         Collection<Artifact> all = new ArrayList<>();
         all.add(artifact);
         all.addAll(Arrays.asList(artifacts));
         return includes(all);
      }

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
         return "Group{" +
                "artifacts=" + artifacts +
                ", versionPropertyName=" + versionPropertyName +
                ", version=" + version +
                ", defaultGroupId=" + defaultGroupId +
                ", defaultScope=" + defaultScope +
                '}';
      }
   }

   public static class PartialGroup {

      private final Group group;

      private PartialGroup(Group group) {
         this.group = group;
      }

      public PartialGroup usingVersionPropertyNamed(Property versionPropertyName) {
         group.usingVersionPropertyNamed(versionPropertyName);
         return this;
      }

      public PartialGroup usingVersionPropertyNamed(String versionPropertyName) {
         group.usingVersionPropertyNamed(versionPropertyName);
         return this;
      }

      public Group atVersion(Property version) {
         return group.atVersion(version).setComplete(true);
      }

      public Group atVersion(String version) {
         return group.atVersion(version).setComplete(true);
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

      public DependencyScope getScope() {
         return Preconditions.checkNotNull(scope != null ? scope : group.getDefaultScope(),
                                           "artifact %s has no scope and its group has no default scope!",
                                           getArtifactId());
      }

      public Artifact artifactId(Property artifactId) {
         Preconditions.checkNotNull(artifactId, "artifactId may not be null!");
         this.artifactId = new PropertyValue(artifactId);
         return this;
      }

      public Artifact artifactId(String artifactId) {
         Preconditions.checkNotNull(artifactId, "artifactId may not be null!");
         Preconditions.checkArgument(!artifactId.trim().isEmpty(), "artifactId may not be empty!");
         this.artifactId = PropertyValue.constant(artifactId);
         return this;
      }

      public Artifact groupId(Property groupId) {
         Preconditions.checkNotNull(groupId, "groupId may not be null!");
         this.groupId = new PropertyValue(groupId);
         return this;
      }

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
         return "Artifact{" +
                ", artifactId=" + artifactId +
                ", groupId=" + groupId +
                ", scope=" + scope +
                '}';
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
         return "Property{" +
                "name='" + name + '\'' +
                '}';
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
         return "PropertyValue{" +
                "property=" + property +
                ", value='" + value + '\'' +
                '}';
      }
   }

   private final static Property CONSTANT_PROPERTY = new Property(null);
   private final static Property OPTIONAL_PROPERTY = new Property(null);
}
