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
            CommonParameters.GROUP_ID.advanced(),
            CommonParameters.ARTIFACT_ID.advanced(),
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
