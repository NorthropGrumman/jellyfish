package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.httpclient;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.CreateJavaServiceGeneratedConfigCommand;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.dto.GeneratedServiceConfigDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.dto.ITransportProviderConfigDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.dto.TransportProviderDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.telemetryreporting.TelemetryReportingDto;
import com.ngc.seaside.jellyfish.service.config.api.ITransportConfigurationService;
import com.ngc.seaside.jellyfish.service.config.api.TransportConfigurationType;
import com.ngc.seaside.jellyfish.service.config.api.dto.RestConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.telemetry.RestTelemetryConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.telemetry.RestTelemetryReportingConfiguration;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class HttpClientTransportProviderConfigDto implements ITransportProviderConfigDto<HttpClientDto> {

   public static final String
         HTTP_CLIENT_TEMPLATE =
         CreateJavaServiceGeneratedConfigCommand.class.getPackage().getName() + "-httpclient";

   private static final String HTTP_CLIENT_TRANSPORT_PROVIDER_COMPONENT_NAME =
         "com.ngc.seaside.service.transport.impl.provider.httpclient.HttpClientTransportProvider";
   private static final String HTTP_CLIENT_CONFIGURATION_CLASS_NAME_SUFFIX = "HttpClientConfiguration";
   private static final String HTTP_CLIENT_TEST_CONFIGURATION_CLASS_NAME_SUFFIX = "HttpClientTestConfiguration";
   private static final String HTTP_CLIENT_PROVIDER_VARIABLE_NAME = "httpClientProvider";
   private static final String
         HTTP_CLIENT_TOPIC =
         "com.ngc.seaside.service.transport.impl.topic.httpclient.HttpClientTopic";
   private static final String
         HTTP_CLIENT_MODULE =
         "com.ngc.seaside.service.transport.impl.provider.httpclient.module.HttpClientTransportProviderModule";
   private static final String HTTP_CLIENT_TOPIC_DEPENDENCY = "com.ngc.seaside:service.transport.impl.topic.httpclient";
   private static final String
         HTTP_CLIENT_PROVIDER_DEPENDENCY =
         "com.ngc.seaside:service.transport.impl.provider.httpclient";
   private static final String
         HTTP_CLIENT_MODULE_DEPENDENCY =
         "com.ngc.seaside:service.transport.impl.provider.httpclient.module";

   private static final String TELEMETRY_TOPIC_DEPENDENCY = "com.ngc.seaside:service.telemetry.api";
   private static final String JSON_TELEMETRY_DEPENDENCY =
            "com.ngc.seaside:service.telemetry.impl.jsontelemetryservice";
   private static final String JSON_TELEMETRY_MODULE_DEPENDENCY =
            "com.ngc.seaside:service.telemetry.impl.jsontelemetryservice.module";
   private static final String TELEMETRY_TOPIC =
            "com.ngc.seaside.service.telemetry.api.ITelemetryService.TELEMETRY_REQUEST_TRANSPORT_TOPIC";
   private ITransportConfigurationService transportConfigurationService;
   private boolean test;

   public HttpClientTransportProviderConfigDto(ITransportConfigurationService transportConfigurationService,
                                               boolean test) {
      this.transportConfigurationService = transportConfigurationService;
      this.test = test;
   }

   @Override
   public TransportProviderDto getTransportProviderDto(HttpClientDto dto) {
      return new TransportProviderDto()
            .setComponentName(HTTP_CLIENT_TRANSPORT_PROVIDER_COMPONENT_NAME)
            .setConfigurationType(dto.getModelName() + getClassnameSuffix())
            .setProviderName(HTTP_CLIENT_PROVIDER_VARIABLE_NAME)
            .setTopic(HTTP_CLIENT_TOPIC)
            .setModule(HTTP_CLIENT_MODULE);
   }

   @Override
   public Optional<HttpClientDto> getConfigurationDto(GeneratedServiceConfigDto dto,
                                                      IJellyFishCommandOptions options, IModel model,
                                                      String topicsClassName,
                                                      Map<String, IDataReferenceField> topics) {
      HttpClientDto httpClientDto = new HttpClientDto().setBaseDto(dto)
            .setTopicsImport(topicsClassName)
            .setClassname(dto.getModelName() + getClassnameSuffix());
      String topicsPrefix = topicsClassName.substring(topicsClassName.lastIndexOf('.') + 1) + '.';

      if (test) {
         int telemetryCount = 1;
         // Add configurations to get the model's telemetry
         for (RestTelemetryConfiguration configuration : getRestTelemetryConfigurations(options, model)) {
            RestConfiguration restConfig = configuration.getConfig();
            String variableName = StringUtils.uncapitalize(model.getName())
                     + "TelemetryResponse"
                     + (telemetryCount > 1 ? telemetryCount : "");
            HttpClientTopicDto topicDto = new HttpClientTopicDto().setNetworkAddress(restConfig.getNetworkAddress())
                     .setNetworkInterface(restConfig.getNetworkInterface())
                     .setPort(restConfig.getPort())
                     .setHttpMethod(restConfig.getHttpMethod())
                     .setPath(restConfig.getPath())
                     .setContentType(restConfig.getContentType())
                     .setVariableName(variableName)
                     .setName(TELEMETRY_TOPIC);
            httpClientDto.addTopic(topicDto);
            telemetryCount++;
         }

         // For models the are composed of parts, add configurations to get the individual part's telemetry
         String topicClass = topicsClassName.substring(topicsClassName.lastIndexOf('.') + 1);
         for (IModelReferenceField part : model.getParts()) {
            Optional<String> telemetryTopic =
                     transportConfigurationService.getTelemetryTransportTopicName(options, part);
            if (!telemetryTopic.isPresent()) {
               continue;
            }
            
            IModel modelPart = part.getType();
            telemetryCount = 1;
            for (RestTelemetryConfiguration configuration : getRestTelemetryConfigurations(options, modelPart)) {
               RestConfiguration restConfig = configuration.getConfig();
               String variableName = StringUtils.uncapitalize(modelPart.getName())
                        + "TelemetryResponse"
                        + (telemetryCount > 1 ? telemetryCount : "");
               HttpClientTopicDto topicDto = new HttpClientTopicDto().setNetworkAddress(restConfig.getNetworkAddress())
                        .setNetworkInterface(restConfig.getNetworkInterface())
                        .setPort(restConfig.getPort())
                        .setHttpMethod(restConfig.getHttpMethod())
                        .setPath(restConfig.getPath())
                        .setContentType(restConfig.getContentType())
                        .setVariableName(variableName)
                        .setName(topicClass + "." + telemetryTopic.get());
               httpClientDto.addTopic(topicDto);
               telemetryCount++;
            }
         }
      } else if (dto.getTelemetryReporting().isPresent()) {
         TelemetryReportingDto reportingDto = dto.getTelemetryReporting().get();
         Set<RestTelemetryReportingConfiguration> reportingConfigs = reportingDto.getConfigs().stream()
                  .filter(RestTelemetryReportingConfiguration.class::isInstance)
                  .map(RestTelemetryReportingConfiguration.class::cast)
                  .collect(Collectors.toCollection(LinkedHashSet::new));
         int telemetryCount = 1;
         for (RestTelemetryReportingConfiguration configuration : reportingConfigs) {
            RestConfiguration restConfig = configuration.getConfig();
            String variableName = StringUtils.uncapitalize(model.getName())
                     + "TelemetryReporting"
                     + (reportingConfigs.size() > 1 ? telemetryCount : "");
            HttpClientTopicDto topicDto = new HttpClientTopicDto().setNetworkAddress(restConfig.getNetworkAddress())
                     .setNetworkInterface(restConfig.getNetworkInterface())
                     .setPort(restConfig.getPort())
                     .setHttpMethod(restConfig.getHttpMethod())
                     .setPath(restConfig.getPath())
                     .setContentType(restConfig.getContentType())
                     .setVariableName(variableName)
                     .setName(reportingDto.getTopic());
            httpClientDto.addTopic(topicDto);
            telemetryCount++;
         }
      }

      for (Map.Entry<String, IDataReferenceField> entry : topics.entrySet()) {
         String topicName = entry.getKey();
         IDataReferenceField field = entry.getValue();
         boolean isOutput = model.getOutputs().contains(field);
         boolean shouldSend = isOutput ^ test;
         if (!shouldSend) {
            // Http Client transport provider can only send, not receive
            continue;
         }
         Collection<RestConfiguration> configurations =
               transportConfigurationService.getRestConfiguration(options, field);
         int count = 1;
         for (RestConfiguration configuration : configurations) {
            HttpClientTopicDto topicDto = new HttpClientTopicDto().setNetworkAddress(configuration.getNetworkAddress())
                  .setNetworkInterface(configuration.getNetworkInterface())
                  .setPort(configuration.getPort())
                  .setHttpMethod(configuration.getHttpMethod())
                  .setPath(configuration.getPath())
                  .setContentType(configuration.getContentType())
                  .setVariableName(
                        field.getName() + (configurations.size() > 1 ? count
                                                                     : ""))
                  .setName(topicsPrefix + topicName);

            httpClientDto.addTopic(topicDto);
            count++;
         }
      }

      if (httpClientDto.getTopics().isEmpty()) {
         return Optional.empty();
      }

      return Optional.of(httpClientDto);
   }

   @Override
   public String getTemplate() {
      return HTTP_CLIENT_TEMPLATE;
   }

   @Override
   public Set<String> getDependencies(IJellyFishCommandOptions options, IModel model, boolean topic, boolean provider,
            boolean module) {
      boolean hasTelemetry = transportConfigurationService == null || transportConfigurationService
               .getConfigurationTypes(options, model).contains(TransportConfigurationType.TELEMETRY);
      for (IModelReferenceField part : model.getParts()) {
         hasTelemetry |= transportConfigurationService.getConfigurationTypes(options, part.getType())
                  .contains(TransportConfigurationType.TELEMETRY);
      }
      Set<String> dependencies = new LinkedHashSet<>();
      if (topic || provider) {
         dependencies.add(HTTP_CLIENT_TOPIC_DEPENDENCY);
         if (hasTelemetry) {
            dependencies.add(TELEMETRY_TOPIC_DEPENDENCY);
         }
      }
      if (provider) {
         dependencies.add(HTTP_CLIENT_PROVIDER_DEPENDENCY);
         if (hasTelemetry) {
            dependencies.add(JSON_TELEMETRY_DEPENDENCY);
         }
      }
      if (module) {
         dependencies.add(HTTP_CLIENT_MODULE_DEPENDENCY);
         if (hasTelemetry) {
            dependencies.add(JSON_TELEMETRY_MODULE_DEPENDENCY);
         }
      }
      return dependencies;
   }

   private String getClassnameSuffix() {
      return test ? HTTP_CLIENT_TEST_CONFIGURATION_CLASS_NAME_SUFFIX : HTTP_CLIENT_CONFIGURATION_CLASS_NAME_SUFFIX;
   }
   
   private Collection<RestTelemetryConfiguration> getRestTelemetryConfigurations(IJellyFishCommandOptions options,
            IModel model) {
      return transportConfigurationService.getTelemetryConfiguration(options, model).stream()
               .filter(RestTelemetryConfiguration.class::isInstance)
               .map(RestTelemetryConfiguration.class::cast)
               .collect(Collectors.toCollection(LinkedHashSet::new));
   }
}
