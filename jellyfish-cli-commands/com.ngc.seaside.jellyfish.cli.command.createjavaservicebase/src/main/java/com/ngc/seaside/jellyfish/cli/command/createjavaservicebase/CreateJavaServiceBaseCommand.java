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
            CommonParameters.GROUP_ID,
            CommonParameters.ARTIFACT_ID,
            CommonParameters.MODEL.required(),
            CommonParameters.OUTPUT_DIRECTORY.required(),
            CommonParameters.CLEAN,
            CommonParameters.UPDATE_GRADLE_SETTING);
   }

   @Override
   protected void runDefaultPhase() {
      IModel model = getModel();
      Path outputDirectory = getOutputDirectory();
      boolean clean = getBooleanParameter(CommonParameters.CLEAN.getName());

      IProjectInformation projectInfo = projectNamingService.getBaseServiceProjectName(getOptions(), model);
      BaseServiceDto dto = templateDaoFactory.newDto(getOptions(), model);

      DefaultParameterCollection parameters = new DefaultParameterCollection();
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

      DefaultParameterCollection parameters = new DefaultParameterCollection();
      parameters.addParameter(new DefaultParameter<>("dto", dto));
      unpackSuffixedTemplate(SERVICE_BASE_GENERATED_BUILD_TEMPLATE_SUFFIX, parameters, outputDirectory, clean);
      if (!dto.isSystem()) {
         unpackSuffixedTemplate(SERVICE_BASE_TEMPLATE_SUFFIX, parameters, outputDirectory, false);
      }
      unpackSuffixedTemplate(TOPICS_TEMPLATE_SUFFIX, parameters, outputDirectory, false);
   }
}
