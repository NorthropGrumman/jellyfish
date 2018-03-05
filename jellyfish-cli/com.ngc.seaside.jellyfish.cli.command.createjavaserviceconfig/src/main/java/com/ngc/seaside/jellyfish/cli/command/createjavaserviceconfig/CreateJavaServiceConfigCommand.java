package com.ngc.seaside.jellyfish.cli.command.createjavaserviceconfig;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateService;
import com.ngc.seaside.jellyfish.api.CommandException;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.createjavaserviceconfig.dto.ServiceConfigDto;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import org.apache.commons.io.FileUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component(service = IJellyFishCommand.class)
public class CreateJavaServiceConfigCommand implements IJellyFishCommand {

   static final String MODEL_PROPERTY = CommonParameters.MODEL.getName();
   static final String OUTPUT_DIRECTORY_PROPERTY = CommonParameters.OUTPUT_DIRECTORY.getName();
   static final String CLEAN_PROPERTY = CommonParameters.CLEAN.getName();

   private static final String NAME = "create-java-service-config";
   private static final IUsage USAGE = createUsage();

   private ILogService logService;
   private ITemplateService templateService;
   private IProjectNamingService projectNamingService;
   private IPackageNamingService packageNamingService;
   private IBuildManagementService buildManagementService;

   @Override
   public String getName() {
      return NAME;
   }

   @Override
   public IUsage getUsage() {
      return USAGE;
   }

   @Override
   public void run(IJellyFishCommandOptions commandOptions) {
      IModel model = evaluateModelParameter(commandOptions);
      boolean clean = CommonParameters.evaluateBooleanParameter(commandOptions.getParameters(), CLEAN_PROPERTY);  
      IProjectInformation projectInfo = projectNamingService.getConfigProjectName(commandOptions, model);
      String packagez = packageNamingService.getConfigPackageName(commandOptions, model);
      Path outputDir = Paths.get(
         commandOptions.getParameters().getParameter(OUTPUT_DIRECTORY_PROPERTY).getStringValue());
      Path projectDir = evaluateProjectDirectory(outputDir, projectInfo.getDirectoryName(), clean);

      ServiceConfigDto dto = new ServiceConfigDto(buildManagementService, commandOptions)
            .setModelName(model.getName())
            .setPackageName(packagez)
            .setBaseProjectArtifactName(projectNamingService.getBaseServiceProjectName(commandOptions, model).getArtifactId())
            .setProjectDirectoryName(projectDir.getFileName().toString());

      DefaultParameterCollection parameters = new DefaultParameterCollection();
      parameters.addParameter(new DefaultParameter<>("dto", dto));
      templateService.unpack(CreateJavaServiceConfigCommand.class.getPackage().getName(),
                             parameters,
                             outputDir,
                             clean);

      buildManagementService.registerProject(commandOptions, projectInfo);
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
   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeTemplateService")
   public void setTemplateService(ITemplateService ref) {
      this.templateService = ref;
   }

   /**
    * Remove template service.
    */
   public void removeTemplateService(ITemplateService ref) {
      setTemplateService(null);
   }
   
   @Reference(cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.STATIC,
            unbind = "removeProjectNamingService")
   public void setProjectNamingService(IProjectNamingService ref) {
      this.projectNamingService = ref;
      
   }

   public void removeProjectNamingService(IProjectNamingService ref) {
      setProjectNamingService(null);
   }
   
   @Reference(cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.STATIC,
            unbind = "removePackageNamingService")
   public void setPackageNamingService(IPackageNamingService ref) {
      this.packageNamingService = ref;
      
   }

   public void removePackageNamingService(IPackageNamingService ref) {
      setPackageNamingService(null);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC)
   public void setBuildManagementService(IBuildManagementService ref) {
      this.buildManagementService = ref;
   }

   public void removeBuildManagementService(IBuildManagementService ref) {
      setBuildManagementService(null);
   }

   private IModel evaluateModelParameter(IJellyFishCommandOptions commandOptions) {
      String modelName = commandOptions.getParameters().getParameter(MODEL_PROPERTY).getStringValue();
      return commandOptions.getSystemDescriptor()
                           .findModel(modelName)
                           .orElseThrow(() -> new CommandException("Unknown model:" + modelName));
   }

   /**
    * Creates and returns the path to the domain project directory.
    *
    * @param outputDir  output directory
    * @param projDirName project directory name
    * @param clean      whether or not to delete the contents of the directory
    * @return the path to the domain project directory
    * @throws CommandException if an error occurred in creating the project directory
    */
   private Path evaluateProjectDirectory(Path outputDir, String projDirName, boolean clean) {
      final Path projectDir = outputDir.resolve(Paths.get(projDirName));
      try {
         Files.createDirectories(outputDir);
         if (clean) {
            FileUtils.deleteQuietly(projectDir.toFile());
         }
         Files.createDirectories(projectDir);
      } catch (IOException e) {
         throw new CommandException(e);
      }
      return projectDir;
   }


   private static IUsage createUsage() {
      return new DefaultUsage(
            "Generates the service configuration for a Java application",
            CommonParameters.GROUP_ID,
            CommonParameters.ARTIFACT_ID,
            CommonParameters.MODEL.required(),
            CommonParameters.OUTPUT_DIRECTORY.required(),
            CommonParameters.CLEAN);
   }
}
