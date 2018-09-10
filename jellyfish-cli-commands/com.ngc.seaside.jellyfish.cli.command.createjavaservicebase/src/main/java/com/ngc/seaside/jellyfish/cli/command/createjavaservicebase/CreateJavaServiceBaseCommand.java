/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.jellyfish.cli.command.createjavaservicebase;

import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto.BaseServiceDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto.IBaseServiceDtoFactory;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;
import com.ngc.seaside.jellyfish.utilities.command.AbstractMultiphaseJellyfishCommand;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.nio.file.Path;

public class CreateJavaServiceBaseCommand extends AbstractMultiphaseJellyfishCommand {

   private static final String NAME = "create-java-service-base";
   static final String SERVICE_BASE_GENERATED_BUILD_TEMPLATE_SUFFIX = "genbuild";
   static final String SERVICE_BASE_BUILD_TEMPLATE_SUFFIX = "build";
   static final String TOPICS_TEMPLATE_SUFFIX = "topics";
   static final String SERVICE_BASE_TEMPLATE_SUFFIX = "servicebase";

   private IBaseServiceDtoFactory templateDaoFactory;

   public CreateJavaServiceBaseCommand() {
      super(NAME);
   }

   @Override
   public void activate() {
      logService.trace(getClass(), "Activated");
   }

   @Override
   public void deactivate() {
      logService.trace(getClass(), "Deactivated");
   }

   public void setTemplateDaoFactory(IBaseServiceDtoFactory ref) {
      this.templateDaoFactory = ref;
   }

   public void removeTemplateDaoFactory(IBaseServiceDtoFactory ref) {
      setTemplateDaoFactory(null);
   }

   @Override
   protected IUsage createUsage() {
      return new DefaultUsage(
            "Generates a Gradle project containing the interface, API, and base classes for a service",
            CommonParameters.GROUP_ID.optional(),
            CommonParameters.ARTIFACT_ID.optional(),
            CommonParameters.MODEL.required(),
            CommonParameters.OUTPUT_DIRECTORY.required(),
            CommonParameters.HEADER_FILE.advanced(),
            CommonParameters.CLEAN.optional(),
            CommonParameters.UPDATE_GRADLE_SETTING.advanced());
   }

   @Override
   protected void runDefaultPhase() {
      IModel model = getModel();
      Path outputDirectory = getOutputDirectory();
      boolean clean = getBooleanParameter(CommonParameters.CLEAN.getName());

      IProjectInformation projectInfo = projectNamingService.getBaseServiceProjectName(getOptions(), model);
      BaseServiceDto dto = templateDaoFactory.newDto(getOptions(), model);

      DefaultParameterCollection parameters = new DefaultParameterCollection(getOptions().getParameters());
      parameters.addParameter(new DefaultParameter<>("dto", dto));
      unpackSuffixedTemplate(SERVICE_BASE_BUILD_TEMPLATE_SUFFIX, parameters, outputDirectory, clean);
      registerProject(projectInfo);
   }

   @Override
   protected void runDeferredPhase() {
      IModel model = getModel();
      Path outputDirectory = getOutputDirectory();
      boolean clean = getBooleanParameter(CommonParameters.CLEAN.getName());

      BaseServiceDto dto = templateDaoFactory.newDto(getOptions(), model);

      DefaultParameterCollection parameters = new DefaultParameterCollection(getOptions().getParameters());
      parameters.addParameter(new DefaultParameter<>("dto", dto));
      unpackSuffixedTemplate(SERVICE_BASE_GENERATED_BUILD_TEMPLATE_SUFFIX, parameters, outputDirectory, clean);
      if (!dto.isSystem()) {
         unpackSuffixedTemplate(SERVICE_BASE_TEMPLATE_SUFFIX, parameters, outputDirectory, false);
      }
      unpackSuffixedTemplate(TOPICS_TEMPLATE_SUFFIX, parameters, outputDirectory, false);
   }
}
