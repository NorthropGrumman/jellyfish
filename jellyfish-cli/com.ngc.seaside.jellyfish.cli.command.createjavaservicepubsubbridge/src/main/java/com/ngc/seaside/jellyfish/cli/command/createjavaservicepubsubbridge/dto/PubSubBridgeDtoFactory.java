package com.ngc.seaside.jellyfish.cli.command.createjavaservicepubsubbridge.dto;

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

public class PubSubBridgeDtoFactory implements IPubSubBridgeDtoFactory {

   private IProjectNamingService projectService;
   private IPackageNamingService packageService;
   private IBuildManagementService buildManagementService;

   @Inject
   public PubSubBridgeDtoFactory(IProjectNamingService projectService,
                            IPackageNamingService packageService,
                            IBuildManagementService buildManagementService) {
      this.projectService = projectService;
      this.packageService = packageService;
      this.buildManagementService = buildManagementService;
   }

   @Override
   public PubSubBridgeDto newDto(IJellyFishCommandOptions options, IModel model, BaseServiceDto baseDto) {
      PubSubBridgeDto dto = new PubSubBridgeDto(buildManagementService, options);

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
      dto.setProjectName(projectService.getServiceProjectName(options, model).getDirectoryName())
            .setService(classDto)
            .setProjectDependencies(projectDependencies)
            .setInterface(baseDto.getInterface().getName())
            .setBaseClass(baseDto.getAbstractClass().getName());

      return dto;
   }

}
