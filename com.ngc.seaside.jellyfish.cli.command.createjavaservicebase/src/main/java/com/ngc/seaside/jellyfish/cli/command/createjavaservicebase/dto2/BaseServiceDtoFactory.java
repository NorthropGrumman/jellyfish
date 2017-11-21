package com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto2;

import com.google.inject.Inject;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.codegen.api.IJavaServiceGenerationService;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

public class BaseServiceDtoFactory {

   private IProjectNamingService projectService;
   private IPackageNamingService packageService;
   private IJavaServiceGenerationService generateService;

   @Inject
   public BaseServiceDtoFactory(IProjectNamingService projectService,
                                IPackageNamingService packageService,
                                IJavaServiceGenerationService generateService) {
      this.projectService = projectService;
      this.packageService = packageService;
      this.generateService = generateService;
   }

   public BaseServiceDto newDto(IJellyFishCommandOptions options, IModel model) {
      return null;
   }

}
