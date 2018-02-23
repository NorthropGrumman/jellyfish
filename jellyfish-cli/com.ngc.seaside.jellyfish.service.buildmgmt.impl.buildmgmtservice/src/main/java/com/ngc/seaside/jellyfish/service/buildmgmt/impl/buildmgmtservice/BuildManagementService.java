package com.ngc.seaside.jellyfish.service.buildmgmt.impl.buildmgmtservice;

import com.google.common.base.Preconditions;

import com.ngc.blocs.json.resource.impl.common.json.IJsonResource;
import com.ngc.blocs.json.resource.impl.common.json.JsonResource;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildDependency;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.buildmgmt.impl.buildmgmtservice.json.ArtifactGroup;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.util.Arrays;
import java.util.Collection;

public class BuildManagementService implements IBuildManagementService {

   private static final String DEPENDENCIES_FILE = "config/app/dependencies.json";

   private Collection<ArtifactGroup> groups;

   private ILogService logService;
   private IResourceService resourceService;

   @Override
   public Collection<IBuildDependency> getProjectDependencies(IJellyFishCommandOptions options) {
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public Collection<IBuildDependency> getBuildDependencies(IJellyFishCommandOptions options) {
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public IBuildDependency getDependency(IJellyFishCommandOptions options, String groupId, String artifactId) {
      Preconditions.checkNotNull(options, "options may not be null!");
      Preconditions.checkNotNull(groupId, "groupId may not be null!");
      Preconditions.checkNotNull(artifactId, "artifactId may not be null!");
      Preconditions.checkArgument(!groupId.trim().isEmpty(), "groupId may not be empty!");
      Preconditions.checkArgument(!artifactId.trim().isEmpty(), "artifactId may not be empty!");

      return groups.stream()
            .flatMap(g -> g.getArtifacts().stream())
            .filter(a -> a.getGroupId().equalsIgnoreCase(groupId) && a.getArtifactId().equalsIgnoreCase(artifactId))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException(String.format("no information for %s:%s configured in %s!",
                                                                          groupId,
                                                                          artifactId,
                                                                          DEPENDENCIES_FILE)));
   }

   @Override
   public IBuildDependency getDependency(IJellyFishCommandOptions options, String groupAndArtifact) {
      Preconditions.checkNotNull(options, "options may not be null!");
      Preconditions.checkNotNull(groupAndArtifact, "groupAndArtifact may not be null!");
      String[] parts = groupAndArtifact.split(":");
      Preconditions.checkArgument(
            parts.length == 2,
            "groupAndArtifact '%s' not in the correct format.  '<groupId>:<artifactId>' is required!",
            groupAndArtifact);
      return getDependency(options, parts[0], parts[1]);
   }

   @Activate
   public void activate() {
      IJsonResource<ArtifactGroup[]> json = new JsonResource<>(DEPENDENCIES_FILE,
                                                               ArtifactGroup[].class);
      if (!resourceService.readFileResource(json)) {
         throw new IllegalStateException(String.format(
               "Failed to read JSON file at '%s', please make sure this file exists is readable by this application!",
               DEPENDENCIES_FILE,
               json.getError()));
      }
      groups = Arrays.asList(json.get());
      groups.forEach(g -> g.getArtifacts().forEach(a -> a.setGroup(g)));

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

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC)
   public void setResourceService(IResourceService ref) {
      this.resourceService = ref;
   }

   public void removeResourceService(IResourceService ref) {
      setResourceService(null);
   }

}
