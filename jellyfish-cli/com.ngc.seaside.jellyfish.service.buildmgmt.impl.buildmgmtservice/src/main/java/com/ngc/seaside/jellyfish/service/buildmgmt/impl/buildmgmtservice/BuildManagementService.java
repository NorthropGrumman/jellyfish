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
package com.ngc.seaside.jellyfish.service.buildmgmt.impl.buildmgmtservice;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.google.common.base.Preconditions;
import com.ngc.seaside.jellyfish.api.ICommandOptions;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.DependencyScope;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildDependency;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.buildmgmt.impl.buildmgmtservice.config.DependenciesConfiguration;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;
import com.ngc.seaside.systemdescriptor.service.log.api.ILogService;

/**
 * A stateful implementation of the {@code IBuildManagementService}.  Unlike most services, a new instance of this
 * service should be used for each generation of project in Jellyfish.  Since Jellyfish only generates a single project
 * per execution, this works fine.
 * <p/>
 * Dependencies and versions are configured by {@link DefaultDependenciesConfiguration}.  That configuration can
 * reference any property declared in {@link #BUILD_PROPERTIES_FILE} as this file will be loaded from the classpath on
 * activation.  This file typically has properties that are set during the build of Jellyfish.
 */
public class BuildManagementService implements IBuildManagementService {

   /**
    * The properties file that should be loaded from the classpath.  This file contains properties that may be
    * referenced in the configuration.
    */
   private static final String BUILD_PROPERTIES_FILE = "com.ngc.seaside.jellyfish.service.buildmgmt.properties";

   /**
    * The registered artifacts.
    */
   private final Set<DependenciesConfiguration.Artifact> registeredArtifacts =
            Collections.synchronizedSet(new TreeSet<>(Comparator
                     .comparing((DependenciesConfiguration.Artifact d) -> d.getGroupId() + d.getArtifactId())));

   /**
    * The registered projects.
    */
   private final Set<IProjectInformation> registeredProjects = Collections.synchronizedSet(new TreeSet<>(
         Comparator.comparing(IProjectInformation::getDirectoryName)));

   /**
    * The configuration.
    */
   private DependenciesConfiguration config;

   private ILogService logService;

   @Override
   public Collection<IBuildDependency> getRegisteredDependencies(ICommandOptions options,
                                                                 DependencyScope scope) {
      Preconditions.checkNotNull(options, "options may not be null!");
      Preconditions.checkNotNull(scope, "scope may not be null!");
      return registeredArtifacts.stream()
            .filter(d -> d.getScope() == scope)
            .collect(Collectors.toList());
   }

   @Override
   public IBuildDependency registerDependency(ICommandOptions options, String groupId, String artifactId) {
      DependenciesConfiguration.Artifact dependency = getDependency(options, groupId, artifactId);
      registeredArtifacts.add(dependency);
      return dependency;
   }

   @Override
   public IBuildDependency registerDependency(ICommandOptions options, String groupAndArtifact) {
      DependenciesConfiguration.Artifact dependency = getDependency(options, groupAndArtifact);
      registeredArtifacts.add(dependency);
      return dependency;
   }

   @Override
   public DependenciesConfiguration.Artifact getDependency(ICommandOptions options,
                                                           String groupId,
                                                           String artifactId) {
      Preconditions.checkNotNull(options, "options may not be null!");
      Preconditions.checkNotNull(groupId, "groupId may not be null!");
      Preconditions.checkNotNull(artifactId, "artifactId may not be null!");
      Preconditions.checkArgument(!groupId.trim().isEmpty(), "groupId may not be empty!");
      Preconditions.checkArgument(!artifactId.trim().isEmpty(), "artifactId may not be empty!");

      return config.getGroups()
            .stream()
            .flatMap(g -> g.getArtifacts().stream())
            .filter(a -> a.getGroupId().equalsIgnoreCase(groupId) && a.getArtifactId()
                  .equalsIgnoreCase(artifactId))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException(
                  String.format(
                        "no information for %s:%s configured; "
                              + "add configuration to the DefaultDependenciesConfiguration class in buildmgmtservice.",
                        groupId,
                        artifactId)));

   }

   @Override
   public DependenciesConfiguration.Artifact getDependency(ICommandOptions options,
                                                           String groupAndArtifact) {
      Preconditions.checkNotNull(options, "options may not be null!");
      Preconditions.checkNotNull(groupAndArtifact, "groupAndArtifact may not be null!");
      String[] parts = groupAndArtifact.split(":");
      Preconditions.checkArgument(
            parts.length == 2,
            "groupAndArtifact '%s' not in the correct format.  '<groupId>:<artifactId>' is required!",
            groupAndArtifact);
      return getDependency(options, parts[0], parts[1]);
   }

   @Override
   public Collection<IProjectInformation> getRegisteredProjects() {
      return registeredProjects;
   }

   @Override
   public void registerProject(ICommandOptions options, IProjectInformation project) {
      Preconditions.checkNotNull(project, "project may not be null!");
      registeredProjects.add(project);
      logService.info(getClass(), "Project %s.%s generated.", project.getGroupId(), project.getArtifactId());
   }

   @Activate
   public void activate() {
      try (InputStream is = getClass().getClassLoader().getResourceAsStream(BUILD_PROPERTIES_FILE)) {
         Properties properties = new Properties();
         properties.load(is);
         // Inject any properties into the configuration.
         config.resolve(properties);
      } catch (IOException e) {
         logService.error(getClass(), e, "Error while reading %s from classpath!", BUILD_PROPERTIES_FILE);
      }
      config.validate();

      logService.debug(getClass(), "Activated.");
   }

   @Deactivate
   public void deactivate() {
      logService.debug(getClass(), "Deactivated.");
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC)
   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

   public void removeLogService(ILogService ref) {
      setLogService(null);
   }

   public void setDependenciesConfiguration(DependenciesConfiguration ref) {
      this.config = ref;
   }

   public void removeDependenciesConfiguration(DependenciesConfiguration ref) {
      setDependenciesConfiguration(null);
   }

}
