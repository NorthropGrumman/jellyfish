package com.ngc.seaside.jellyfish.cli.command.createjavaservice.dto;

import com.google.inject.Inject;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.codegen.api.IJavaServiceGenerationService;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.ClassDto;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.MethodDto;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ServiceDtoFactory implements IServiceDtoFactory {

   private IProjectNamingService projectService;
   private IPackageNamingService packageService;
   private IJavaServiceGenerationService generateService;

   @Inject
   public ServiceDtoFactory(IProjectNamingService projectService,
                            IPackageNamingService packageService,
                            IJavaServiceGenerationService generateService) {
      this.projectService = projectService;
      this.packageService = packageService;
      this.generateService = generateService;
   }

   @Override
   public ServiceDto newDto(IJellyFishCommandOptions options, IModel model) {
      ServiceDto dto = new ServiceDto();

      ClassDto<?> abstractClassDto = generateService.getBaseServiceDescription(options, model);
      ClassDto<? extends MethodDto> interfaceDto = generateService.getServiceInterfaceDescription(options, model);

      List<? extends MethodDto> methods = interfaceDto.getMethods();
      methods.forEach(method -> method.setOverride(true));

      Set<String> imports = new LinkedHashSet<>();
      imports.add(interfaceDto.getPackageName() + "." + interfaceDto.getName());
      imports.add(abstractClassDto.getPackageName() + "." + abstractClassDto.getName());
      imports.addAll(interfaceDto.getImports());

      Set<String> projectDependencies = new LinkedHashSet<>();
      projectDependencies.add(projectService.getEventsProjectName(options, model).getArtifactId());
      projectDependencies.add(projectService.getBaseServiceProjectName(options, model).getArtifactId());

      ClassDto<MethodDto> classDto = new ClassDto<>();
      classDto.setName(model.getName())
              .setPackageName(packageService.getServiceImplementationPackageName(options, model))
              .setBaseClass(abstractClassDto)
              .setImplementedInterface(interfaceDto)
              .setMethods(methods)
              .setImports(imports);

      dto.setProjectDirectoryName(projectService.getServiceProjectName(options, model).getDirectoryName())
         .setService(classDto)
         .setProjectDependencies(projectDependencies);

      return dto;
   }

}
