package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.sparktelemetry;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.CreateJavaServiceGeneratedConfigCommand;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.dto.GeneratedServiceConfigDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.dto.ITransportProviderConfigDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.dto.TransportProviderDto;
import com.ngc.seaside.jellyfish.service.config.api.ITransportConfigurationService;
import com.ngc.seaside.jellyfish.service.config.api.dto.RestConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.telemetry.RestTelemetryConfiguration;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class RestTelemetryTransportProviderConfigDto implements ITransportProviderConfigDto<RestTelemetryDto> {

   public static final String TEMPLATE =
         CreateJavaServiceGeneratedConfigCommand.class.getPackage().getName() + "-spark";

   private static final String TRANSPORT_PROVIDER_COMPONENT_NAME =
         "com.ngc.seaside.service.transport.impl.provider.spark.SparkTransportProvider";
   private static final String CONFIGURATION_CLASS_NAME_SUFFIX = "TelemetryConfiguration";
   private static final String PROVIDER_VARIABLE_NAME = "restTelemetryProvider";
   private static final String TOPIC = "com.ngc.seaside.service.transport.impl.topic.spark.SparkTopic";
   private static final String SPARK_MODULE =
         "com.ngc.seaside.service.transport.impl.provider.spark.module.SparkTransportProviderModule";
   private static final String TOPIC_DEPENDENCY = "com.ngc.seaside:service.transport.impl.topic.spark";
   private static final String PROVIDER_DEPENDENCY = "com.ngc.seaside:service.transport.impl.provider.spark";
   private static final String MODULE_DEPENDENCY = "com.ngc.seaside:service.transport.impl.provider.spark.module";
   private static final String SL4J_LOG_SERVICE_BRIDGE_DEPENDENCY =
         "com.ngc.seaside:service.log.impl.common.sl4jlogservicebridge";
   private static final String SPARK_CORE_DEPENDENCY = "com.sparkjava:spark-core";
   private static final String TELEMETRY_SERVICE_QUALIFIED_NAME = "com.ngc.seaside.service.telemetry.api.ITelemetryService";
   private static final String TELEMETRY_TOPIC = "ITelemetryService.TELEMETRY_REQUEST_TRANSPORT_TOPIC";

   private ITransportConfigurationService transportConfigurationService;

   public RestTelemetryTransportProviderConfigDto(ITransportConfigurationService transportConfigurationService) {
      this.transportConfigurationService = transportConfigurationService;   }

   @Override
   public TransportProviderDto getTransportProviderDto(RestTelemetryDto dto) {
      return new TransportProviderDto()
            .setComponentName(TRANSPORT_PROVIDER_COMPONENT_NAME)
            .setConfigurationType(dto.getModelName() + getClassnameSuffix())
            .setProviderName(PROVIDER_VARIABLE_NAME)
            .setTopic(TOPIC)
            .setModule(SPARK_MODULE);
   }

   @Override
   public Optional<RestTelemetryDto> getConfigurationDto(GeneratedServiceConfigDto dto,
                                                 IJellyFishCommandOptions options, IModel model, String topicsClassName,
                                                 Map<String, IDataReferenceField> topics) {
      RestTelemetryDto telemetryDto = new RestTelemetryDto().setBaseDto(dto)
            .setTopicsImport(TELEMETRY_SERVICE_QUALIFIED_NAME)
            .setClassname(dto.getModelName() + getClassnameSuffix());
      

      Collection<RestTelemetryConfiguration> configurations =
               transportConfigurationService.getTelemetryConfiguration(options, model).stream()
               .filter(RestTelemetryConfiguration.class::isInstance)
               .map(RestTelemetryConfiguration.class::cast)
               .collect(Collectors.toCollection(LinkedHashSet::new));
      
      int count = 1;
      for (RestTelemetryConfiguration configuration : configurations) {
         RestConfiguration restConfig = configuration.getConfig();
         RestTelemetryTopicDto topicDto = new RestTelemetryTopicDto().setNetworkAddress(restConfig.getNetworkAddress())
               .setNetworkInterface(restConfig.getNetworkInterface())
               .setPort(restConfig.getPort())
               .setHttpMethod(restConfig.getHttpMethod())
               .setPath(restConfig.getPath())
               .setContentType(restConfig.getContentType())
               .setVariableName(model.getName() + (configurations.size() > 1 ? count : ""))
               .setName(TELEMETRY_TOPIC);

         telemetryDto.addTopic(topicDto);
         count++;
      }
      
      if (telemetryDto.getTopics().isEmpty()) {
         return Optional.empty();
      }

      return Optional.of(telemetryDto);
   }

   @Override
   public String getTemplate() {
      return TEMPLATE;
   }

   @Override
   public Set<String> getDependencies(boolean topic, boolean provider, boolean module) {
      Set<String> dependencies = new LinkedHashSet<>();
      if (topic || provider) {
         dependencies.add(TOPIC_DEPENDENCY);
      }
      if (provider) {
         dependencies.add(PROVIDER_DEPENDENCY);
         dependencies.add(SL4J_LOG_SERVICE_BRIDGE_DEPENDENCY);
         //dependencies.add(SPARK_CORE_DEPENDENCY);
      }
      if (module) {
         dependencies.add(MODULE_DEPENDENCY);
      }
      return dependencies;
   }

   private String getClassnameSuffix() {
      return CONFIGURATION_CLASS_NAME_SUFFIX;
   }
}
