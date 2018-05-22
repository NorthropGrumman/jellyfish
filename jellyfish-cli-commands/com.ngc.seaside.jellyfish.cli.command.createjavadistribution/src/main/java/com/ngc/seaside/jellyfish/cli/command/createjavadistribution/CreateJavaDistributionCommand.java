package com.ngc.seaside.jellyfish.cli.command.createjavadistribution;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.CommandException;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.cli.command.createjavadistribution.dto.ConfigDto;
import com.ngc.seaside.jellyfish.cli.command.createjavadistribution.dto.TransportProviderDependenciesUtil;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.config.api.ITransportConfigurationService;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateService;
import com.ngc.seaside.jellyfish.utilities.command.AbstractJellyfishCommand;
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
import java.util.LinkedHashSet;
import java.util.Set;

@Component(service = IJellyFishCommand.class)
public class CreateJavaDistributionCommand extends AbstractJellyfishCommand implements IJellyFishCommand {

   public static final String GROUP_ID_PROPERTY = CommonParameters.GROUP_ID.getName();
   public static final String ARTIFACT_ID_PROPERTY = CommonParameters.ARTIFACT_ID.getName();
   public static final String OUTPUT_DIRECTORY_PROPERTY = CommonParameters.OUTPUT_DIRECTORY.getName();
   public static final String MODEL_PROPERTY = CommonParameters.MODEL.getName();
   public static final String MODEL_OBJECT_PROPERTY = CommonParameters.MODEL_OBJECT.getName();
   public static final String PACKAGE_PROPERTY = CommonParameters.PACKAGE.getName();
   public static final String CLEAN_PROPERTY = CommonParameters.CLEAN.getName();
   static final String CREATE_SERVICE_DOMAIN_PROPERTY = "createServiceDomain";

   private static final String NAME = "create-java-distribution";

   private ITransportConfigurationService transportConfigService;

   public CreateJavaDistributionCommand() {
      super(NAME);
   }

   @Override
   protected void doRun() {
      IJellyFishCommandOptions options = getOptions();
      DefaultParameterCollection parameters = new DefaultParameterCollection();
      parameters.addParameters(options.getParameters().getAllParameters());

      final IModel model = getModel();

      parameters.addParameter(new DefaultParameter<>(MODEL_OBJECT_PROPERTY, model));

      IProjectInformation info = projectNamingService.getDistributionProjectName(options, model);

      final Path outputDirectory = getOutputDirectory();

      doCreateDirectories(outputDirectory);

      // Resolve clean property
      final boolean clean = getBooleanParameter(CLEAN_PROPERTY);

      String pkg = packageNamingService.getDistributionPackageName(options, model);
      parameters.addParameter(new DefaultParameter<>(PACKAGE_PROPERTY, pkg));

      ConfigDto dto = new ConfigDto(buildManagementService, options);

      dto.setProjectName(info.getDirectoryName());
      dto.setPackageName(pkg);
      dto.setModel(model);
      dto.setTransportProviderDependencies(
            TransportProviderDependenciesUtil.getTransportProviderDependencies(options, transportConfigService));

      Set<String> projectDependencies = new LinkedHashSet<String>();
      projectDependencies.add(projectNamingService.getEventsProjectName(options, model).getArtifactId());
      if (CommonParameters.evaluateBooleanParameter(options.getParameters(), CREATE_SERVICE_DOMAIN_PROPERTY, true)) {
         projectDependencies.add(projectNamingService.getDomainProjectName(options, model).getArtifactId());
      }
      projectDependencies.add(projectNamingService.getConnectorProjectName(options, model).getArtifactId());
      projectDependencies.add(projectNamingService.getConfigProjectName(options, model).getArtifactId());
      projectDependencies.add(projectNamingService.getBaseServiceProjectName(options, model).getArtifactId());
      projectDependencies.add(projectNamingService.getMessageProjectName(options, model).getArtifactId());
      projectDependencies.add(projectNamingService.getServiceProjectName(options, model).getArtifactId());
      projectDependencies.add(projectNamingService.getPubSubBridgeProjectName(options, model).getArtifactId());
      dto.setProjectDependencies(projectDependencies);

      parameters.addParameter(new DefaultParameter<>("dto", dto));
      unpackDefaultTemplate(parameters, outputDirectory, clean);
      registerProject(info);
   }

   @Activate
   public void activate() {
      super.activate();
      logService.trace(getClass(), "Activated");
   }

   @Deactivate
   public void deactivate() {
      super.deactivate();
      logService.trace(getClass(), "Deactivated");
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeLogService")
   public void setLogService(ILogService ref) {
      super.setLogService(ref);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeTemplateService")
   public void setTemplateService(ITemplateService ref) {
      super.setTemplateService(ref);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeProjectNamingService")
   public void setProjectNamingService(IProjectNamingService ref) {
      super.setProjectNamingService(ref);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removePackageNamingService")
   public void setPackageNamingService(IPackageNamingService ref) {
      super.setPackageNamingService(ref);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeBuildManagementService")
   public void setBuildManagementService(IBuildManagementService ref) {
      super.setBuildManagementService(ref);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeTransportConfigurationService")
   public void setTransportConfigurationService(ITransportConfigurationService ref) {
      this.transportConfigService = ref;
   }

   public void removeTransportConfigurationService(ITransportConfigurationService ref) {
      setTransportConfigurationService(null);
   }

   protected void doCreateDirectories(Path outputDirectory) {
      try {
         Files.createDirectories(outputDirectory);
      } catch (IOException e) {
         logService.error(CreateJavaDistributionCommand.class, e);
         throw new CommandException(e);
      }
   }

   @Override
   protected IUsage createUsage() {
      return new DefaultUsage("Generates the gradle distribution project for a Java application",
                              CommonParameters.GROUP_ID,
                              CommonParameters.ARTIFACT_ID,
                              CommonParameters.OUTPUT_DIRECTORY.required(),
                              CommonParameters.MODEL.required(),
                              CommonParameters.CLEAN
      );
   }
}
