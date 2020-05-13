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

      ClassDto adviser = new ClassDto()
            .setName(model.getName() + "Adviser");
      dto.setAdviser(adviser);
      adviser.getImports().add(baseDto.getInterface().getPackageName()
                               + "."
                               + baseDto.getInterface().getName()
                               + "Adviser");

      return dto;
   }

}
