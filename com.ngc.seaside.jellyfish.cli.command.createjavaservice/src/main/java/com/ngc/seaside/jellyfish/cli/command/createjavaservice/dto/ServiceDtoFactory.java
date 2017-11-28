package com.ngc.seaside.jellyfish.cli.command.createjavaservice.dto;

import com.google.inject.Inject;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto.BaseServiceDto;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.ClassDto;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.MethodDto;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ServiceDtoFactory implements IServiceDtoFactory {

   private IProjectNamingService projectService;
   private IPackageNamingService packageService;

   @Inject
   public ServiceDtoFactory(IProjectNamingService projectService,
                            IPackageNamingService packageService) {
      this.projectService = projectService;
      this.packageService = packageService;
   }

   @Override
   public ServiceDto newDto(IJellyFishCommandOptions options, IModel model, BaseServiceDto baseDto) {
      ServiceDto dto = new ServiceDto();

      Set<String> imports = new LinkedHashSet<>();
      imports.add(baseDto.getInterface().getFullyQualifiedName());
      imports.add(baseDto.getAbstractClass().getFullyQualifiedName());
      imports.addAll(baseDto.getInterface().getImports());

      Set<String> projectDependencies = new LinkedHashSet<>();
      projectDependencies.add(projectService.getEventsProjectName(options, model).getArtifactId());
      projectDependencies.add(projectService.getBaseServiceProjectName(options, model).getArtifactId());

      List<MethodDto> methods = model.getScenarios()
                                     .stream()
                                     .map(IScenario::getName)
                                     .map(scenario -> new MethodDto().setName(scenario))
                                     .collect(Collectors.toList());

      ClassDto<MethodDto> classDto = new ClassDto<>();
      classDto.setName(model.getName())
              .setPackageName(packageService.getServiceImplementationPackageName(options, model))
              .setBaseClass(baseDto.getAbstractClass())
              .setMethods(methods)
              .setImplementedInterface(baseDto.getInterface())
              .setImports(imports);

      dto.setProjectDirectoryName(projectService.getServiceProjectName(options, model).getDirectoryName())
         .setService(classDto)
         .setProjectDependencies(projectDependencies);

      return dto;
   }

}
