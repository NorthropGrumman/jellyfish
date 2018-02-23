package com.ngc.seaside.jellyfish.service.buildmgmt.impl.buildmgmtservice;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildDependency;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.util.Collection;

public class BuildManagementService implements IBuildManagementService {

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
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public IBuildDependency getDependency(IJellyFishCommandOptions options, String groupAndArtifact) {
      throw new UnsupportedOperationException("not implemented");
   }

   @Activate
   public void activate() {
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
