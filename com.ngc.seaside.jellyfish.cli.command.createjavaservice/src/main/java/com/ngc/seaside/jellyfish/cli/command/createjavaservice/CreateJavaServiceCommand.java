package com.ngc.seaside.jellyfish.cli.command.createjavaservice;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.bootstrap.utilities.file.FileUtilitiesException;
import com.ngc.seaside.bootstrap.utilities.file.GradleSettingsUtilities;
import com.ngc.seaside.command.api.CommandException;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.createjavaservice.dto.IServiceDtoFactory;
import com.ngc.seaside.jellyfish.cli.command.createjavaservice.dto.ServiceDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto.BaseServiceDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto.IBaseServiceDtoFactory;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.nio.file.Path;
import java.nio.file.Paths;

@Component(service = IJellyFishCommand.class)
public class CreateJavaServiceCommand implements IJellyFishCommand {

   static final String GROUP_ID_PROPERTY = CommonParameters.GROUP_ID.getName();
   static final String ARTIFACT_ID_PROPERTY = CommonParameters.ARTIFACT_ID.getName();
   static final String MODEL_PROPERTY = CommonParameters.MODEL.getName();
   static final String OUTPUT_DIRECTORY_PROPERTY = CommonParameters.OUTPUT_DIRECTORY.getName();
   static final String CLEAN_PROPERTY = CommonParameters.CLEAN.getName();

   private static final String NAME = "create-java-service";
   private static final IUsage USAGE = createUsage();

   private ILogService logService;
   private ITemplateService templateService;
   private IServiceDtoFactory serviceTemplateDaoFactory;
   private IBaseServiceDtoFactory baseServiceTemplateDaoFactory;
   private IProjectNamingService projectNamingService;

   @Override
   public void run(IJellyFishCommandOptions commandOptions) {
      IModel model = evaluateModelParameter(commandOptions);
      boolean clean = CommonParameters.evaluateBooleanParameter(commandOptions.getParameters(), CLEAN_PROPERTY);
      Path outputDir = Paths.get(
         commandOptions.getParameters().getParameter(OUTPUT_DIRECTORY_PROPERTY).getStringValue());

      IProjectInformation projectInfo = projectNamingService.getServiceProjectName(commandOptions, model);

      BaseServiceDto baseServiceDto = baseServiceTemplateDaoFactory.newDto(commandOptions, model);
      ServiceDto serviceDto = serviceTemplateDaoFactory.newDto(commandOptions, model, baseServiceDto);
      

      DefaultParameterCollection parameters = new DefaultParameterCollection();
      parameters.addParameter(new DefaultParameter<>("serviceDto", serviceDto));
      parameters.addParameter(new DefaultParameter<>("baseServiceDto", baseServiceDto));
      templateService.unpack(CreateJavaServiceCommand.class.getPackage().getName(),
         parameters,
         outputDir,
         clean);

      updateGradleDotSettings(outputDir, projectInfo);
   }
   
   private void updateGradleDotSettings(Path outputDir, IProjectInformation info) {
      DefaultParameterCollection updatedParameters = new DefaultParameterCollection();
      updatedParameters.addParameter(new DefaultParameter<>(OUTPUT_DIRECTORY_PROPERTY,
         outputDir.resolve(info.getDirectoryName()).getParent().toString()));
      updatedParameters.addParameter(new DefaultParameter<>(GROUP_ID_PROPERTY, info.getGroupId()));
      updatedParameters.addParameter(new DefaultParameter<>(ARTIFACT_ID_PROPERTY, info.getArtifactId()));
      try {
         if (!GradleSettingsUtilities.tryAddProject(updatedParameters)) {
            logService.warn(getClass(), "Unable to add the new project to settings.gradle.");
         }
      } catch (FileUtilitiesException e) {
         throw new CommandException("failed to update settings.gradle!", e);
      }
   }

   @Override
   public String getName() {
      return NAME;
   }

   @Override
   public IUsage getUsage() {
      return USAGE;
   }

   @Activate
   public void activate() {
      logService.trace(getClass(), "Activated");
   }

   @Deactivate
   public void deactivate() {
      logService.trace(getClass(), "Deactivated");
   }

   /**
    * Sets log service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeLogService")
   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

   /**
    * Remove log service.
    */
   public void removeLogService(ILogService ref) {
      setLogService(null);
   }

   /**
    * Sets template service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeTemplateService")
   public void setTemplateService(ITemplateService ref) {
      this.templateService = ref;
   }

   /**
    * Remove template service.
    */
   public void removeTemplateService(ITemplateService ref) {
      setTemplateService(null);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeServiceTemplateDaoFactory")
   public void setServiceTemplateDaoFactory(IServiceDtoFactory ref) {
      this.serviceTemplateDaoFactory = ref;
   }

   public void removeServiceTemplateDaoFactory(IServiceDtoFactory ref) {
      setServiceTemplateDaoFactory(null);
   }
   
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeBaseServiceTemplateDaoFactory")
   public void setBaseServiceTemplateDaoFactory(IBaseServiceDtoFactory ref) {
      this.baseServiceTemplateDaoFactory = ref;
   }

   public void removeBaseServiceTemplateDaoFactory(IBaseServiceDtoFactory ref) {
      setBaseServiceTemplateDaoFactory(null);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
   public void setProjectNamingService(IProjectNamingService ref) {
      this.projectNamingService = ref;
   }

   public void removeProjectNamingService(IProjectNamingService ref) {
      setProjectNamingService(null);
   }

   private IModel evaluateModelParameter(IJellyFishCommandOptions commandOptions) {
      String modelName = commandOptions.getParameters().getParameter(MODEL_PROPERTY).getStringValue();
      return commandOptions.getSystemDescriptor()
                           .findModel(modelName)
                           .orElseThrow(() -> new CommandException("Unknown model:" + modelName));
   }

   private static IUsage createUsage() {
      return new DefaultUsage(
         "Generates the service for a Java application",
         CommonParameters.GROUP_ID,
         CommonParameters.ARTIFACT_ID,
         CommonParameters.MODEL.required(),
         CommonParameters.OUTPUT_DIRECTORY.required(),
         CommonParameters.CLEAN);
   }

}
