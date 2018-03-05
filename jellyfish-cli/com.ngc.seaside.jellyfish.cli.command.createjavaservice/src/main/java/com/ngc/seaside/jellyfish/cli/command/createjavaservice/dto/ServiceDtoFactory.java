package com.ngc.seaside.jellyfish.cli.command.createjavaservice.dto;

import com.google.inject.Inject;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto.BaseServiceDto;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.ClassDto;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.LinkedHashSet;
import java.util.Set;

public class ServiceDtoFactory implements IServiceDtoFactory {

   private IProjectNamingService projectService;
   private IPackageNamingService packageService;
   private IBuildManagementService buildManagementService;

   @Inject
   public ServiceDtoFactory(IProjectNamingService projectService,
                            IPackageNamingService packageService,
                            IBuildManagementService buildManagementService) {
      this.projectService = projectService;
      this.packageService = packageService;
      this.buildManagementService = buildManagementService;
   }

   @Override
   public ServiceDto newDto(IJellyFishCommandOptions options, IModel model, BaseServiceDto baseDto) {
      ServiceDto dto = new ServiceDto(buildManagementService, options);

      Set<String> imports = new LinkedHashSet<>();
      imports.add(baseDto.getInterface().getFullyQualifiedName());
      imports.add(baseDto.getAbstractClass().getFullyQualifiedName());
      imports.addAll(baseDto.getInterface().getImports());

      Set<String> projectDependencies = new LinkedHashSet<>();
      projectDependencies.add(projectService.getEventsProjectName(options, model).getArtifactId());
      projectDependencies.add(projectService.getBaseServiceProjectName(options, model).getArtifactId());

      ClassDto classDto = new ClassDto();
      classDto.setName(model.getName())
            .setPackageName(packageService.getServiceImplementationPackageName(options, model))
            .setImports(imports);
      dto.setProjectDirectoryName(projectService.getServiceProjectName(options, model).getDirectoryName())
            .setService(classDto)
            .setProjectDependencies(projectDependencies)
            .setInterface(baseDto.getInterface().getName())
            .setBaseClass(baseDto.getAbstractClass().getName());
      dto.getService().getImports().add(baseDto.getInterface().getFullyQualifiedName());
      dto.getService().getImports().add(baseDto.getAbstractClass().getFullyQualifiedName());

      return dto;
   }

}
