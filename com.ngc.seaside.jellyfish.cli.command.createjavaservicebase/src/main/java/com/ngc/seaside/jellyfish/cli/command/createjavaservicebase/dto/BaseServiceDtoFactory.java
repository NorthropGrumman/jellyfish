package com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto;

import com.google.inject.Inject;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.codegen.api.IJavaServiceGenerationService;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.ClassDto;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.EnumDto;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.MethodDto;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.PubSubMethodDto;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class BaseServiceDtoFactory implements IBaseServiceDtoFactory {

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

   @Override
   public BaseServiceDto newDto(IJellyFishCommandOptions options, IModel model) {
      BaseServiceDto dto = new BaseServiceDto();

      Set<String> projectDependencies = Collections.singleton(
         projectService.getEventsProjectName(options, model).getArtifactId());

      ClassDto<? extends MethodDto> interfaceDto = generateService.getServiceInterfaceDescription(options, model);
      ClassDto<? extends PubSubMethodDto> abstractClassDto = generateService.getBaseServiceDescription(options, model);
      EnumDto<?> topicsDto = generateService.getTransportTopicsDescription(options, model);

      dto.setProjectDirectoryName(projectService.getBaseServiceProjectName(options, model).getDirectoryName())
         .setProjectDependencies(projectDependencies)
         .setAbstractClass(abstractClassDto)
         .setInterface(interfaceDto)
         .setExportedPackages(new LinkedHashSet<>(
            Arrays.asList(packageService.getServiceBaseImplementationPackageName(options, model) + ".*",
               packageService.getServiceImplementationPackageName(options, model) + ".*",
               packageService.getTransportTopicsPackageName(options, model) + ".*")))
         .setTopicsEnum(topicsDto)
         .setModel(model);

      return dto;
   }

}
