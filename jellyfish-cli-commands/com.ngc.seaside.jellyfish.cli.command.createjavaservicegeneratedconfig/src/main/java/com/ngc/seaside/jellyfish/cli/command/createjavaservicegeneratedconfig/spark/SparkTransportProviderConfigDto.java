package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.spark;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.CreateJavaServiceGeneratedConfigCommand;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.dto.GeneratedServiceConfigDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.dto.ITransportProviderConfigDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.dto.TransportProviderDto;
import com.ngc.seaside.jellyfish.service.config.api.ITransportConfigurationService;
import com.ngc.seaside.jellyfish.service.config.api.TransportConfigurationType;
import com.ngc.seaside.jellyfish.service.config.api.dto.RestConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.telemetry.RestTelemetryConfiguration;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class SparkTransportProviderConfigDto implements ITransportProviderConfigDto<SparkDto> {

   public static final String
         SPARK_TEMPLATE =
         CreateJavaServiceGeneratedConfigCommand.class.getPackage().getName() + "-spark";

   private static final String SPARK_TRANSPORT_PROVIDER_COMPONENT_NAME =
         "com.ngc.seaside.service.transport.impl.provider.spark.SparkTransportProvider";
   private static final String SPARK_CONFIGURATION_CLASS_NAME_SUFFIX = "SparkConfiguration";
   private static final String SPARK_TEST_CONFIGURATION_CLASS_NAME_SUFFIX = "SparkTestConfiguration";
   private static final String SPARK_PROVIDER_VARIABLE_NAME = "sparkProvider";
   private static final String SPARK_TOPIC = "com.ngc.seaside.service.transport.impl.topic.spark.SparkTopic";
   private static final String
         SPARK_MODULE =
         "com.ngc.seaside.service.transport.impl.provider.spark.module.SparkTransportProviderModule";
   private static final String SPARK_TOPIC_DEPENDENCY = "com.ngc.seaside:service.transport.impl.topic.spark";
   private static final String TELEMETRY_TOPIC_DEPENDENCY = "com.ngc.seaside:service.telemetry.api";
   private static final String SPARK_PROVIDER_DEPENDENCY = "com.ngc.seaside:service.transport.impl.provider.spark";
   private static final String SPARK_MODULE_DEPENDENCY = "com.ngc.seaside:service.transport.impl.provider.spark.module";
   private static final String
         SL4J_LOG_SERVICE_BRIDGE_DEPENDENCY =
         "com.ngc.seaside:service.log.impl.common.sl4jlogservicebridge";
   private static final String SPARK_CORE_DEPENDENCY = "com.sparkjava:spark-core";
   private static final String TELEMETRY_SERVICE_QUALIFIED_NAME = "com.ngc.seaside.service.telemetry.api.ITelemetryService";
   private static final String TELEMETRY_TOPIC = "ITelemetryService.TELEMETRY_REQUEST_TRANSPORT_TOPIC";

   private ITransportConfigurationService transportConfigurationService;
   private boolean test;

   public SparkTransportProviderConfigDto(ITransportConfigurationService transportConfigurationService, boolean test) {
      this.transportConfigurationService = transportConfigurationService;
      this.test = test;
   }

   @Override
   public TransportProviderDto getTransportProviderDto(SparkDto dto) {
      return new TransportProviderDto()
            .setComponentName(SPARK_TRANSPORT_PROVIDER_COMPONENT_NAME)
            .setConfigurationType(dto.getModelName() + getClassnameSuffix())
            .setProviderName(SPARK_PROVIDER_VARIABLE_NAME)
            .setTopic(SPARK_TOPIC)
            .setModule(SPARK_MODULE);
   }

   @Override
   public Optional<SparkDto> getConfigurationDto(GeneratedServiceConfigDto dto,
                                                 IJellyFishCommandOptions options, IModel model, String topicsClassName,
                                                 Map<String, IDataReferenceField> topics) {
      SparkDto sparkDto = new SparkDto().setBaseDto(dto)
            .addImport(topicsClassName)
            .setClassname(dto.getModelName() + getClassnameSuffix());
      String topicsPrefix = topicsClassName.substring(topicsClassName.lastIndexOf('.') + 1) + '.';

      Collection<RestTelemetryConfiguration> telemetryConfigurations =
               transportConfigurationService.getTelemetryConfiguration(options, model).stream()
               .filter(RestTelemetryConfiguration.class::isInstance)
               .map(RestTelemetryConfiguration.class::cast)
               .collect(Collectors.toCollection(LinkedHashSet::new));
      
      int count = 1;
      for (RestTelemetryConfiguration configuration : telemetryConfigurations) {
         RestConfiguration restConfig = configuration.getConfig();
         SparkTopicDto topicDto = new SparkTopicDto().setNetworkAddress(restConfig.getNetworkAddress())
               .setNetworkInterface(restConfig.getNetworkInterface())
               .setPort(restConfig.getPort())
               .setHttpMethod(restConfig.getHttpMethod())
               .setPath(restConfig.getPath())
               .setContentType(restConfig.getContentType())
                                                     .setVariableName(StringUtils.uncapitalize(model.getName())
                                                        + (telemetryConfigurations.size() > 1 ? count : ""))
                                                     .setName(TELEMETRY_TOPIC);

         sparkDto.addTopic(topicDto);
         sparkDto.addImport(TELEMETRY_SERVICE_QUALIFIED_NAME);
         count++;
      }
      
      for (Map.Entry<String, IDataReferenceField> entry : topics.entrySet()) {
         String topicName = entry.getKey();
         IDataReferenceField field = entry.getValue();
         boolean isOutput = model.getOutputs().contains(field);
         boolean shouldReceive = !(isOutput ^ test);
         if (!shouldReceive) {
            // Spark transport provider can only receive, not send
            continue;
         }
         Collection<RestConfiguration> configurations =
               transportConfigurationService.getRestConfiguration(options, field);
         count = 1;
         for (RestConfiguration configuration : configurations) {
            SparkTopicDto topicDto = new SparkTopicDto().setNetworkAddress(configuration.getNetworkAddress())
                  .setNetworkInterface(configuration.getNetworkInterface())
                  .setPort(configuration.getPort())
                  .setHttpMethod(configuration.getHttpMethod())
                  .setPath(configuration.getPath())
                  .setContentType(configuration.getContentType())
                  .setVariableName(
                        field.getName() + (configurations.size() > 1 ? count
                                                                     : ""))
                  .setName(topicsPrefix + topicName);

            sparkDto.addTopic(topicDto);
            count++;
         }
      }

      if (sparkDto.getTopics().isEmpty()) {
         return Optional.empty();
      }

      return Optional.of(sparkDto);
   }

   @Override
   public String getTemplate() {
      return SPARK_TEMPLATE;
   }

   @Override
   public Set<String> getDependencies(IJellyFishCommandOptions options, IModel model, boolean topic, boolean provider,
            boolean module) {
      Set<String> dependencies = new LinkedHashSet<>();
      if (topic || provider) {
         dependencies.add(SPARK_TOPIC_DEPENDENCY);
         if (transportConfigurationService == null
            || transportConfigurationService.getConfigurationTypes(options, model)
                                            .contains(TransportConfigurationType.TELEMETRY)) {
            dependencies.add(TELEMETRY_TOPIC_DEPENDENCY);
         }
      }
      if (provider) {
         dependencies.add(SPARK_PROVIDER_DEPENDENCY);
         dependencies.add(SL4J_LOG_SERVICE_BRIDGE_DEPENDENCY);
         //dependencies.add(SPARK_CORE_DEPENDENCY);
      }
      if (module) {
         dependencies.add(SPARK_MODULE_DEPENDENCY);
      }
      return dependencies;
   }

   private String getClassnameSuffix() {
      return test ? SPARK_TEST_CONFIGURATION_CLASS_NAME_SUFFIX : SPARK_CONFIGURATION_CLASS_NAME_SUFFIX;
   }
}
