package com.ngc.seaside.jellyfish.cli.command.createjavadistribution;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateService;
import com.ngc.seaside.jellyfish.utilities.file.FileUtilitiesException;
import com.ngc.seaside.jellyfish.utilities.file.GradleSettingsUtilities;
import com.ngc.seaside.jellyfish.api.CommandException;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.createjavadistribution.dto.ConfigDto;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

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
import java.util.LinkedHashSet;
import java.util.Set;

@Component(service = IJellyFishCommand.class)
public class CreateJavaDistributionCommand implements IJellyFishCommand {

   public static final String GROUP_ID_PROPERTY = CommonParameters.GROUP_ID.getName();
   public static final String ARTIFACT_ID_PROPERTY = CommonParameters.ARTIFACT_ID.getName();
   public static final String OUTPUT_DIRECTORY_PROPERTY = CommonParameters.OUTPUT_DIRECTORY.getName();
   public static final String MODEL_PROPERTY = CommonParameters.MODEL.getName();
   public static final String MODEL_OBJECT_PROPERTY = CommonParameters.MODEL_OBJECT.getName();
   public static final String PACKAGE_PROPERTY = CommonParameters.PACKAGE.getName();
   public static final String CLEAN_PROPERTY = CommonParameters.CLEAN.getName();
   static final String CREATE_SERVICE_DOMAIN_PROPERTY = "createServiceDomain";

   private static final String NAME = "create-java-distribution";
   private static final IUsage USAGE = createUsage();
   private ILogService logService;
   private ITemplateService templateService;
   private IProjectNamingService projectNamingService;
   private IPackageNamingService packageNamingService;
   private IBuildManagementService buildManagementService;


   /**
    * Create the usage for this command.
    *
    * @return the usage.
    */
   private static IUsage createUsage() {
      return new DefaultUsage("Generates the gradle distribution project for a Java application",
            CommonParameters.GROUP_ID, 
            CommonParameters.ARTIFACT_ID, 
            CommonParameters.OUTPUT_DIRECTORY.required(), 
            CommonParameters.MODEL.required(), 
            CommonParameters.CLEAN
      );
   }

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
      DefaultParameterCollection parameters = new DefaultParameterCollection();
      parameters.addParameters(commandOptions.getParameters().getAllParameters());

      ISystemDescriptor systemDescriptor = commandOptions.getSystemDescriptor();
      String modelId = parameters.getParameter(MODEL_PROPERTY).getStringValue();

      final IModel model = systemDescriptor.findModel(modelId)
                                           .orElseThrow(() -> new CommandException("Unknown model:" + modelId));

      parameters.addParameter(new DefaultParameter<>(MODEL_OBJECT_PROPERTY, model));

      IProjectInformation info = projectNamingService.getDistributionProjectName(commandOptions, model);

      final Path outputDirectory = Paths.get(parameters.getParameter(OUTPUT_DIRECTORY_PROPERTY).getStringValue());

      doCreateDirectories(outputDirectory);

      // Resolve clean property
      final boolean clean = CommonParameters.evaluateBooleanParameter(parameters, CLEAN_PROPERTY);

      String pkg = packageNamingService.getDistributionPackageName(commandOptions, model);
      parameters.addParameter(new DefaultParameter<>(PACKAGE_PROPERTY, pkg));

      ConfigDto dto = new ConfigDto(buildManagementService, commandOptions);
      
      dto.setProjectName(info.getDirectoryName());
      dto.setPackageName(pkg);
      dto.setModel(model);
      
      Set<String> projectDependencies = new LinkedHashSet<String>();
      projectDependencies.add(projectNamingService.getEventsProjectName(commandOptions, model).getArtifactId());
      if (CommonParameters.evaluateBooleanParameter(commandOptions.getParameters(), CREATE_SERVICE_DOMAIN_PROPERTY, true)) {
         projectDependencies.add(projectNamingService.getDomainProjectName(commandOptions, model).getArtifactId());
      }
      projectDependencies.add(projectNamingService.getConnectorProjectName(commandOptions, model).getArtifactId());
      projectDependencies.add(projectNamingService.getConfigProjectName(commandOptions, model).getArtifactId());
      projectDependencies.add(projectNamingService.getBaseServiceProjectName(commandOptions, model).getArtifactId());
      projectDependencies.add(projectNamingService.getMessageProjectName(commandOptions, model).getArtifactId());
      projectDependencies.add(projectNamingService.getServiceProjectName(commandOptions, model).getArtifactId());
      dto.setProjectDependencies(projectDependencies);
      
      
      
      parameters.addParameter(new DefaultParameter<>("dto", dto));

      templateService.unpack(CreateJavaDistributionCommand.class.getPackage().getName(),
                             parameters,
                             outputDirectory,
                             clean);
      
      updateGradleDotSettings(outputDirectory, info);
      
      logService.info(CreateJavaDistributionCommand.class, "%s distribution project successfully created",
                      model.getName());
   }
   
   protected void updateGradleDotSettings(Path outputDir, IProjectInformation info) {
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

   /**
    * Sets project naming service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.STATIC,
            unbind = "removeProjectNamingService")
   public void setProjectNamingService(IProjectNamingService ref) {
      this.projectNamingService = ref;
      
   }
   
   /**
    * Remove project naming service.
    */
   public void removeProjectNamingService(IProjectNamingService ref) {
      setProjectNamingService(null);
   }
   
   /**
    * Sets package naming service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.STATIC,
            unbind = "removePackageNamingService")
   public void setPackageNamingService(IPackageNamingService ref) {
      this.packageNamingService = ref;
      
   }
   
   /**
    * Remove package naming service.
    */
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

   protected void doCreateDirectories(Path outputDirectory) {
      try {
         Files.createDirectories(outputDirectory);
      } catch (IOException e) {
         logService.error(CreateJavaDistributionCommand.class, e);
         throw new CommandException(e);
      }
   }
}

