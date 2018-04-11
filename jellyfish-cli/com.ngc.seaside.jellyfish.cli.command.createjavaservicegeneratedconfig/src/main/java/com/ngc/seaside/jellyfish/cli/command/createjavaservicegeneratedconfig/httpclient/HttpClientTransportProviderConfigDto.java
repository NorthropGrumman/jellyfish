package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.httpclient;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.CreateJavaServiceGeneratedConfigCommand;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.dto.GeneratedServiceConfigDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.dto.ITransportProviderConfigDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.dto.TransportProviderDto;
import com.ngc.seaside.jellyfish.service.config.api.ITransportConfigurationService;
import com.ngc.seaside.jellyfish.service.config.api.dto.RestConfiguration;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class HttpClientTransportProviderConfigDto implements ITransportProviderConfigDto<HttpClientDto> {
   public static final String HTTP_CLIENT_TEMPLATE = CreateJavaServiceGeneratedConfigCommand.class.getPackage().getName() + "-httpclient";

   private static final String HTTP_CLIENT_TRANSPORT_PROVIDER_COMPONENT_NAME =
         "com.ngc.seaside.service.transport.impl.provider.httpclient.HttpClientTransportProvider";
   private static final String HTTP_CLIENT_CONFIGURATION_CLASS_NAME_SUFFIX = "HttpClientConfiguration";
   private static final String HTTP_CLIENT_TEST_CONFIGURATION_CLASS_NAME_SUFFIX = "HttpClientTestConfiguration";
   private final static String HTTP_CLIENT_PROVIDER_VARIABLE_NAME = "httpClientProvider";
   private static final String HTTP_CLIENT_TOPIC = "com.ngc.seaside.service.transport.impl.topic.httpclient.HttpClientTopic";
   private static final String HTTP_CLIENT_MODULE = "com.ngc.seaside.service.transport.impl.provider.httpclient.module.HttpClientTransportProviderModule";
   private static final String HTTP_CLIENT_TOPIC_DEPENDENCY = "com.ngc.seaside:service.transport.impl.topic.httpclient";
   private static final String HTTP_CLIENT_PROVIDER_DEPENDENCY = "com.ngc.seaside:service.transport.impl.provider.httpclient";
   private static final String HTTP_CLIENT_MODULE_DEPENDENCY = "com.ngc.seaside:service.transport.impl.provider.httpclient.module";

   private ITransportConfigurationService transportConfigurationService;
   private boolean test;

   public HttpClientTransportProviderConfigDto(ITransportConfigurationService transportConfigurationService, boolean test) {
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
                                                 IJellyFishCommandOptions options, IModel model, String topicsClassName,
                                                 Map<String, IDataReferenceField> topics) {
      HttpClientDto httpClientDto = new HttpClientDto().setBaseDto(dto)
                                                  .setTopicsImport(topicsClassName)
                                                  .setClassname(dto.getModelName() + getClassnameSuffix());
      String topicsPrefix = topicsClassName.substring(topicsClassName.lastIndexOf('.') + 1) + '.';

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
   public Set<String> getDependencies(boolean topic, boolean provider, boolean module) {
      Set<String> dependencies = new LinkedHashSet<>();
      if (topic || provider) {
         dependencies.add(HTTP_CLIENT_TOPIC_DEPENDENCY);
      }
      if (provider) {
         dependencies.add(HTTP_CLIENT_PROVIDER_DEPENDENCY);
      }
      if (module) {
         dependencies.add(HTTP_CLIENT_MODULE_DEPENDENCY);
      }
      return dependencies;
   }

   private String getClassnameSuffix() {
      return test ? HTTP_CLIENT_TEST_CONFIGURATION_CLASS_NAME_SUFFIX : HTTP_CLIENT_CONFIGURATION_CLASS_NAME_SUFFIX;
   }
}
