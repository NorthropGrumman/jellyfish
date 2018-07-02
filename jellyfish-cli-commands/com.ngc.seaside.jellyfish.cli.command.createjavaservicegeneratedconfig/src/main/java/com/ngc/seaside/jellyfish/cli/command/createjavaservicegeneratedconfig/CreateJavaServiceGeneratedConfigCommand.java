package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.CommandException;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto.BaseServiceDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto.BasicPubSubDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto.CorrelationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto.IBaseServiceDtoFactory;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto.InputDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.dto.GeneratedServiceConfigDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.dto.ITransportProviderConfigDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.httpclient.HttpClientTransportProviderConfigDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.multicast.MulticastTransportProviderConfigDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.spark.SparkTransportProviderConfigDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.telemetry.TelemetryDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.telemetryreporting.TelemetryReportingDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.zeromq.ZeroMqTransportProviderConfigDto;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.CommonDependencies;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.codegen.api.IJavaServiceGenerationService;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.EnumDto;
import com.ngc.seaside.jellyfish.service.config.api.ITransportConfigurationService;
import com.ngc.seaside.jellyfish.service.config.api.TransportConfigurationType;
import com.ngc.seaside.jellyfish.service.config.api.dto.telemetry.TelemetryReportingConfiguration;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;
import com.ngc.seaside.jellyfish.service.scenario.api.IMessagingFlow;
import com.ngc.seaside.jellyfish.service.scenario.api.IScenarioService;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateService;
import com.ngc.seaside.jellyfish.utilities.command.AbstractMultiphaseJellyfishCommand;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component(service = IJellyFishCommand.class)
public class CreateJavaServiceGeneratedConfigCommand extends AbstractMultiphaseJellyfishCommand
      implements IJellyFishCommand {

   static final String CONFIG_GENERATED_BUILD_TEMPLATE_SUFFIX = "genbuild";
   static final String CONFIG_BUILD_TEMPLATE_SUFFIX = "build";
   static final String TELEMETRY_CONFIG_TEMPLATE_SUFFIX = "telemetry";
   static final String TELEMETRY_REPORTING_CONFIG_TEMPLATE_SUFFIX = "telemetryreporting";

   static final String MODEL_PROPERTY = CommonParameters.MODEL.getName();
   static final String DEPLOYMENT_MODEL_PROPERTY = CommonParameters.DEPLOYMENT_MODEL.getName();
   static final String OUTPUT_DIRECTORY_PROPERTY = CommonParameters.OUTPUT_DIRECTORY.getName();
   static final String CLEAN_PROPERTY = CommonParameters.CLEAN.getName();

   static final String NAME = "create-java-service-generated-config";

   private static final String CONNECTOR_SUFFIX = "Connector";
   private static final String SUBSCRIBER_SUFFIX = "Subscriber";

   private IBaseServiceDtoFactory baseServiceDtoFactory;
   private ITransportConfigurationService transportConfigService;
   private IScenarioService scenarioService;
   private IJavaServiceGenerationService generateService;

   protected CreateJavaServiceGeneratedConfigCommand() {
      super(NAME);
   }

   @Override
   protected void runDefaultPhase() {
      IJellyFishCommandOptions options = getOptions();
      IModel model = getModel();
      Path outputDirectory = getOutputDirectory();
      boolean clean = getBooleanParameter(CLEAN_PROPERTY);

      IProjectInformation projectInfo = projectNamingService.getGeneratedConfigProjectName(options, model);
      GeneratedServiceConfigDto dto = new GeneratedServiceConfigDto(buildManagementService, options);
      dto.setProjectDirectoryName(projectInfo.getDirectoryName());

      DefaultParameterCollection parameters = new DefaultParameterCollection();
      parameters.addParameter(new DefaultParameter<>("dto", dto));
      unpackSuffixedTemplate(CONFIG_BUILD_TEMPLATE_SUFFIX, parameters, outputDirectory, clean);
      buildManagementService.registerDependency(options, CommonDependencies.SPARK_CORE.getGropuId(),
               CommonDependencies.SPARK_CORE.getArtifactId());
      registerProject(projectInfo);
   }

   @Override
   protected void runDeferredPhase() {
      IJellyFishCommandOptions options = getOptions();
      IModel model = getModel();
      boolean clean = getBooleanParameter(CLEAN_PROPERTY);
      Path outputDir = getOutputDirectory();

      IProjectInformation projectInfo = projectNamingService.getGeneratedConfigProjectName(options, model);
      IProjectInformation connectorInfo = projectNamingService.getConnectorProjectName(options, model);
      String packagez = packageNamingService.getConfigPackageName(options, model);
      Path projectDir = evaluateProjectDirectory(outputDir, projectInfo.getDirectoryName(), clean);

      GeneratedServiceConfigDto dto = new GeneratedServiceConfigDto(buildManagementService, options)
            .setModel(model)
            .setPackageName(packagez)
            .setBaseProjectArtifactName(
                  projectNamingService.getBaseServiceProjectName(options, model).getArtifactId())
            .setProjectDirectoryName(outputDir.relativize(projectDir).toString())
            .setTelemetry(transportConfigService.getConfigurationTypes(getOptions(), model)
                          .contains(TransportConfigurationType.TELEMETRY))
            .setConnectorClassname(
                  String.format("%s.%s.%s%s",
                                connectorInfo.getGroupId(),
                                connectorInfo.getArtifactId(),
                                model.getName(), CONNECTOR_SUFFIX))
            .setTelemetry(transportConfigService.getConfigurationTypes(getOptions(), model)
                                .contains(TransportConfigurationType.TELEMETRY));

      BaseServiceDto baseServiceDto = baseServiceDtoFactory.newDto(getOptions(), model);
      List<CorrelationDto> correlationMethodDtos = baseServiceDto.getCorrelationMethods();
      List<BasicPubSubDto> pubSubMethods = baseServiceDto.getBasicPubSubMethods();
      String packageName = packageNamingService.getPubSubBridgePackageName(options, model);

      for (CorrelationDto correlationMethodDto : correlationMethodDtos) {
         for (InputDto inputDto : correlationMethodDto.getInputs()) {
            dto.addReadinessSubscriber(
                  String.format("%s.%s%s", packageName, inputDto.getType(), SUBSCRIBER_SUFFIX));
         }
      }

      for (BasicPubSubDto pubSubMethod : pubSubMethods) {
         dto.addReadinessSubscriber(
               String.format("%s.%s%s", packageName, pubSubMethod.getInput().getType(), SUBSCRIBER_SUFFIX));
      }

      if (transportConfigService.getConfigurationTypes(getOptions(), model)
               .contains(TransportConfigurationType.TELEMETRY_REPORTING)) {
         Collection<TelemetryReportingConfiguration> reportingConfigs =
                  transportConfigService.getTelemetryReportingConfiguration(getOptions(), model);
         long rates = reportingConfigs.stream().mapToInt(TelemetryReportingConfiguration::getRateInSeconds).distinct()
                  .count();
         if (rates > 1) {
            logService.warn(getClass(), "Cannot generate multiple telemetry reporting with different rates");
         } else if (rates == 1) {
            TelemetryReportingDto reportingDto = new TelemetryReportingDto();
            reportingDto.setBaseDto(dto);
            reportingDto.setClassname("TelemetryReporting");
            int rate = reportingConfigs.iterator().next().getRateInSeconds();
            reportingDto.setRateInMilliseconds(1000 * rate);
            EnumDto topicsEnum = baseServiceDto.getTopicsEnum();
            reportingDto.setTopicsImport(topicsEnum.getFullyQualifiedName());
            String topicName =
                     transportConfigService.getTelemetryReportingTransportTopicName(getOptions(), model).get();
            reportingDto.setTopic(topicsEnum.getName() + "." + topicName);
            reportingConfigs.forEach(reportingDto::addConfig);
            reportingDto.addReadinessConfigurations();
            dto.setTelemetryReporting(reportingDto);
         }
      }

      Collection<ITransportProviderConfigDto<?>> transportProviders = Arrays.asList(
            new MulticastTransportProviderConfigDto(transportConfigService, false),
            new SparkTransportProviderConfigDto(transportConfigService, false),
            new HttpClientTransportProviderConfigDto(transportConfigService, false),
            new ZeroMqTransportProviderConfigDto(transportConfigService, scenarioService, false));

      clean = generateAndAddTransportProviders(
            dto,
            options,
            outputDir,
            clean,
            model,
            transportProviders);

      TelemetryDto telemetry = new TelemetryDto();
      if (dto.hasTelemetry()) {
         telemetry.setBaseDto(dto)
                  .setClassname(model.getName() + "TelemetryConfiguration")
                  .addReadinessConfigurations();
      }

      DefaultParameterCollection parameters = new DefaultParameterCollection();
      parameters.addParameter(new DefaultParameter<>("dto", dto));
      parameters.addParameter(new DefaultParameter<>("StringUtils", StringUtils.class));
      unpackSuffixedTemplate(CONFIG_GENERATED_BUILD_TEMPLATE_SUFFIX, parameters, outputDir, clean);

      if (dto.hasTelemetry()) {
         DefaultParameterCollection telemetryParams = new DefaultParameterCollection();
         telemetryParams.addParameter(new DefaultParameter<>("dto", telemetry));
         unpackSuffixedTemplate(TELEMETRY_CONFIG_TEMPLATE_SUFFIX, telemetryParams, outputDir, false);
      }
      if (dto.getTelemetryReporting().isPresent()) {
         DefaultParameterCollection telemetryParams = new DefaultParameterCollection();
         telemetryParams.addParameter(new DefaultParameter<>("dto", dto.getTelemetryReporting().get()));
         unpackSuffixedTemplate(TELEMETRY_REPORTING_CONFIG_TEMPLATE_SUFFIX, telemetryParams, outputDir, false);
      }

      registerProject(projectInfo);
   }

   @Activate
   public void activate() {
      logService.trace(getClass(), "Activated");
   }

   @Deactivate
   public void deactivate() {
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

   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
   public void setBuildManagementService(IBuildManagementService ref) {
      super.setBuildManagementService(ref);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
   public void setTransportConfigurationService(ITransportConfigurationService ref) {
      this.transportConfigService = ref;
   }

   public void removeTransportConfigurationService(ITransportConfigurationService ref) {
      setTransportConfigurationService(null);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
   public void setJavaServiceGenerationService(IJavaServiceGenerationService ref) {
      this.generateService = ref;
   }

   public void removeJavaServiceGenerationService(IJavaServiceGenerationService ref) {
      setJavaServiceGenerationService(null);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
   public void setScenarioService(IScenarioService ref) {
      this.scenarioService = ref;
   }

   public void removeScenarioService(IScenarioService ref) {
      setScenarioService(null);
   }

   public void setBaseServiceDtoFactory(IBaseServiceDtoFactory ref) {
      this.baseServiceDtoFactory = ref;
   }

   public void removeBaseServiceDtoFactory(IBaseServiceDtoFactory ref) {
      setBaseServiceDtoFactory(null);
   }

   @SuppressWarnings({"unchecked", "rawtypes"})
   private boolean generateAndAddTransportProviders(GeneratedServiceConfigDto dto,
                                                    IJellyFishCommandOptions options,
                                                    Path outputDirectory,
                                                    boolean clean,
                                                    IModel model,
                                                    Collection<ITransportProviderConfigDto<?>> transportProviders) {
      EnumDto transportTopicsClass = generateService.getTransportTopicsDescription(options, model);

      Map<String, IDataReferenceField> topics = new LinkedHashMap<>();

      for (IScenario scenario : model.getScenarios()) {
         scenarioService.getPubSubMessagingFlow(options, scenario).ifPresent(flow -> {
            for (IDataReferenceField input : flow.getInputs()) {
               addToTopicsMap(topics, flow, input);
            }
            for (IDataReferenceField output : flow.getOutputs()) {
               addToTopicsMap(topics, flow, output);
            }
         });
         scenarioService.getRequestResponseMessagingFlow(options, scenario).ifPresent(flow -> {
            addToTopicsMap(topics, flow, flow.getInput());
            addToTopicsMap(topics, flow, flow.getOutput());
         });
      }

      String topicsClassName = transportTopicsClass.getFullyQualifiedName();
      for (ITransportProviderConfigDto transportProvider : transportProviders) {
         Optional<Object> object = transportProvider.getConfigurationDto(dto, options, model, topicsClassName, topics);
         if (object.isPresent()) {
            DefaultParameterCollection parameters = new DefaultParameterCollection();
            parameters.addParameter(new DefaultParameter<>("dto", object.get()));
            String templateName = transportProvider.getTemplate();
            templateService.unpack(templateName, parameters, outputDirectory, clean);
            dto.addTransportProvider(transportProvider.getTransportProviderDto(object.get()));
            dto.addTransportProviderDependencies(transportProvider.getDependencies(options, model, true, false, false));
            dto.addDistributionDependencies(transportProvider.getDependencies(options, model, false, true, false));
            clean = false;
         }
      }

      return clean;
   }

   /**
    * Gets the topic name for the given flow and field and adds the topic with the field to the given map.
    *
    * @throws IllegalStateException if the map already contains the given topic with a different field
    */
   private void addToTopicsMap(Map<String, IDataReferenceField> map, IMessagingFlow flow, IDataReferenceField field) {
      String topicName = transportConfigService.getTransportTopicName(flow, field);
      IDataReferenceField previous = map.put(topicName, field);
      if (previous != null && !Objects.equals(previous, field)) {
         throw new IllegalStateException(
               String.format("Two data reference fields assigned to the same topic %s: %s and %s",
                             topicName,
                             field.getName(),
                             previous.getName()));
      }
   }

   /**
    * Creates and returns the path to the domain project directory.
    *
    * @param outputDir   output directory
    * @param projDirName project directory name
    * @param clean       whether or not to delete the contents of the directory
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

   @Override
   protected IUsage createUsage() {
      return new DefaultUsage(
            "Generates the generated service configuration for a Java application",
            CommonParameters.GROUP_ID,
            CommonParameters.ARTIFACT_ID,
            CommonParameters.MODEL.required(),
            CommonParameters.DEPLOYMENT_MODEL.required(),
            CommonParameters.OUTPUT_DIRECTORY.required(),
            CommonParameters.CLEAN);
   }
}
