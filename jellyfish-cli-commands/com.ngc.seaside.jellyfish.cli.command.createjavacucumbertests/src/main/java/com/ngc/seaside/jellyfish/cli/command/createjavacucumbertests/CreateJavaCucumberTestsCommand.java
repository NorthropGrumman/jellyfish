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
package com.ngc.seaside.jellyfish.cli.command.createjavacucumbertests;

import com.ngc.seaside.jellyfish.api.CommandException;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.api.ParameterCategory;
import com.ngc.seaside.jellyfish.cli.command.createjavacucumbertests.dto.CucumberDto;
import com.ngc.seaside.jellyfish.service.codegen.api.IJavaServiceGenerationService;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.ClassDto;
import com.ngc.seaside.jellyfish.service.config.api.ITelemetryConfigurationService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;
import com.ngc.seaside.jellyfish.utilities.command.AbstractJellyfishCommand;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Optional;

@Component(service = IJellyFishCommand.class)
public class CreateJavaCucumberTestsCommand extends AbstractJellyfishCommand {

   private static final String NAME = "create-java-cucumber-tests";

   public static final String OUTPUT_DIRECTORY_PROPERTY = CommonParameters.OUTPUT_DIRECTORY.getName();
   public static final String MODEL_PROPERTY = CommonParameters.MODEL.getName();
   public static final String CLEAN_PROPERTY = CommonParameters.CLEAN.getName();
   public static final String DEPLOYMENT_MODEL = CommonParameters.DEPLOYMENT_MODEL.getName();
   public static final String REFRESH_FEATURE_FILES_PROPERTY = "refreshFeatureFiles";
   public static final String BUILD_TEMPLATE_SUFFIX = "build";
   public static final String CONFIG_TEMPLATE_SUFFIX = "config";

   public static final String MODEL_OBJECT_PROPERTY = "modelObject";

   private IJavaServiceGenerationService generationService;
   private ITelemetryConfigurationService telemetryConfigService;

   public CreateJavaCucumberTestsCommand() {
      super(NAME);
   }

   @Reference
   public void setJavaServiceGenerationService(IJavaServiceGenerationService ref) {
      this.generationService = ref;
   }

   public void removeJavaServiceGenerationService(IJavaServiceGenerationService ref) {
      setJavaServiceGenerationService(null);
   }

   @Reference
   public void setTelemetryConfigService(ITelemetryConfigurationService ref) {
      this.telemetryConfigService = ref;
   }

   public void removeTelemetryConfigService(ITelemetryConfigurationService ref) {
      setTelemetryConfigService(null);
   }

   protected void doCreateDirectories(Path outputDirectory) {
      try {
         Files.createDirectories(outputDirectory);
      } catch (IOException e) {
         logService.error(CreateJavaCucumberTestsCommand.class, e);
         throw new CommandException(e);
      }
   }

   @Override
   protected IUsage createUsage() {
      return new DefaultUsage("Generates the Gradle project responsible for testing a service's acceptance tests",
                              CommonParameters.GROUP_ID.advanced(),
                              CommonParameters.ARTIFACT_ID.advanced(),
                              CommonParameters.OUTPUT_DIRECTORY.required(),
                              CommonParameters.MODEL.required(),
                              CommonParameters.CLEAN.optional(),
                              CommonParameters.HEADER_FILE.optional(),
                              new DefaultParameter<>(REFRESH_FEATURE_FILES_PROPERTY).setDescription(
                                    "If true, only copy the feature files and resources from the system descriptor "
                                    + "project into src/main/resources.")
                                    .setParameterCategory(ParameterCategory.ADVANCED));
   }

   @Override
   protected void doRun() {
      IJellyFishCommandOptions commandOptions = getOptions();

      DefaultParameterCollection parameters = new DefaultParameterCollection();
      parameters.addParameters(commandOptions.getParameters().getAllParameters());

      IModel model = getModel();
      parameters.addParameter(new DefaultParameter<>(MODEL_OBJECT_PROPERTY, model));

      final Path outputDirectory = getOutputDirectory();
      doCreateDirectories(outputDirectory);

      IProjectInformation info = projectNamingService.getCucumberTestsProjectName(commandOptions, model);

      final String packageName = packageNamingService.getCucumberTestsPackageName(commandOptions, model);
      final String projectName = info.getDirectoryName();
      final boolean clean = CommonParameters.evaluateBooleanParameter(commandOptions.getParameters(), CLEAN_PROPERTY);

      boolean isConfigGenerated = parameters.getParameter(CommonParameters.DEPLOYMENT_MODEL.getName()) != null
                                  && parameters.getParameter(CommonParameters.DEPLOYMENT_MODEL.getName()).getValue()
                                     != null;

      CucumberDto dto = new CucumberDto(buildManagementService, commandOptions)
            .setProjectName(projectName)
            .setPackageName(packageName)
            .setClassName(model.getName())
            .setTransportTopicsClass(
                  generationService.getTransportTopicsDescription(commandOptions, model).getFullyQualifiedName())
            .setConfigGenerated(isConfigGenerated)
            .setDependencies(new LinkedHashSet<>(Arrays.asList(
                  projectNamingService.getMessageProjectName(commandOptions, model)
                        .getArtifactId(),
                  projectNamingService.getBaseServiceProjectName(commandOptions, model)
                        .getArtifactId())));
      String configModule = packageNamingService.getCucumberTestsConfigPackageName(commandOptions, model) + "."
                            + model.getName() + "TestConfigurationModule";
      dto.setConfigModule(configModule);

      ClassDto adviser = new ClassDto();
      ClassDto adviserImpl = new ClassDto();
      if (dto.isSystem()) {
         adviser.setPackageName(packageNamingService.getCucumberTestsConfigPackageName(commandOptions, model)
                                + "."
                                + model.getName()
                                + "TestTransportConfiguration")
               .setTypeName("I" + dto.getClassName() + "Adviser");
         adviserImpl.setPackageName(dto.getPackageName() + ".di")
               .setTypeName(dto.getClassName() + "TestAdviser");
      } else {
         ClassDto interfacez = generationService.getServiceInterfaceDescription(commandOptions, model);
         adviser.setPackageName(interfacez.getPackageName())
               .setTypeName(interfacez.getTypeName() + "Adviser");
         adviserImpl.setPackageName(dto.getPackageName() + ".di")
               .setTypeName(dto.getClassName() + "TestAdviser");
      }
      dto.setAdviser(adviser)
            .setAdviserImpl(adviserImpl);


      parameters.addParameter(new DefaultParameter<>("dto", dto));

      if (isConfigGenerated) {
         String pkg = packageNamingService.getCucumberTestsConfigPackageName(commandOptions, model);
         dto.setConfigPackageName(pkg);
         dto.getDependencies()
               .add(projectNamingService.getCucumberTestsConfigProjectName(commandOptions, model).getArtifactId());
      } else {
         dto.setConfigPackageName(dto.getPackageName() + ".config");
      }
      if (dto.isSystem()) {
         Collection<IModelReferenceField> parts = getModel().getParts();
         if (parts != null) {
            String transportTopics = generationService.getTransportTopicsDescription(commandOptions, model).getName();
            dto.getImports().add(dto.getTransportTopicsClass());
            for (IModelReferenceField part : getModel().getParts()) {
               Optional<String> name = telemetryConfigService.getTransportTopicName(commandOptions, part);
               if (name.isPresent()) {
                  dto.addRemoteService(transportTopics + "." + name.get());
               }
            }
         }
      } else {
         if (!telemetryConfigService.getConfigurations(commandOptions, model).isEmpty()) {
            dto.getImports().add("com.ngc.seaside.service.telemetry.api.ITelemetryService");
            dto.addRemoteService("ITelemetryService.TELEMETRY_REQUEST_TRANSPORT_TOPIC");
         }
      }

      unpackSuffixedTemplate(BUILD_TEMPLATE_SUFFIX, parameters, outputDirectory, clean);
      if (!isConfigGenerated) {
         unpackSuffixedTemplate(CONFIG_TEMPLATE_SUFFIX, parameters, outputDirectory, false);
      }
      buildManagementService.registerProject(commandOptions, info);
   }
}
