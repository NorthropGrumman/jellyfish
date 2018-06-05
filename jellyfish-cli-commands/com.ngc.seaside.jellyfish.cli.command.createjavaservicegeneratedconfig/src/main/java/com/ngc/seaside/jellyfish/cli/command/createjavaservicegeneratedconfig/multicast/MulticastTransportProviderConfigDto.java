package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.multicast;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.CreateJavaServiceGeneratedConfigCommand;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.dto.GeneratedServiceConfigDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.dto.ITransportProviderConfigDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.dto.TransportProviderDto;
import com.ngc.seaside.jellyfish.service.config.api.ITransportConfigurationService;
import com.ngc.seaside.jellyfish.service.config.api.dto.MulticastConfiguration;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class MulticastTransportProviderConfigDto implements ITransportProviderConfigDto<MulticastDto> {

   public static final String
         MULTICAST_TEMPLATE =
         CreateJavaServiceGeneratedConfigCommand.class.getPackage().getName() + "-multicast";

   static final String
         MULTICAST_TRANSPORT_PROVIDER_COMPONENT_NAME =
         "com.ngc.seaside.service.transport.impl.provider.multicast.MulticastTransportProvider";
   static final String MULTICAST_CONFIGURATION_CLASS_NAME_SUFFIX = "MulticastConfiguration";
   static final String MULTICAST_TEST_CONFIGURATION_CLASS_NAME_SUFFIX = "MulticastTestsConfiguration";
   static final String MULTICAST_PROVIDER_VARIABLE_NAME = "multicastProvider";
   static final String MULTICAST_TOPIC = "com.ngc.seaside.service.transport.impl.topic.multicast.MulticastTopic";
   static final String
         MULTICAST_PROVIDER_MODULE =
         "com.ngc.seaside.service.transport.impl.provider.multicast.module.MulticastTransportProviderModule";
   static final String MULTICAST_TOPIC_DEPENDENCY = "com.ngc.seaside:service.transport.impl.topic.multicast";
   static final String MULTICAST_PROVIDER_DEPENDENCY = "com.ngc.seaside:service.transport.impl.provider.multicast";
   static final String MULTICAST_MODULE_DEPENDENCY = "com.ngc.seaside:service.transport.impl.provider.multicast.module";


   private ITransportConfigurationService transportConfigService;

   private final boolean test;

   public MulticastTransportProviderConfigDto(ITransportConfigurationService transportConfigService, boolean test) {
      this.transportConfigService = transportConfigService;
      this.test = test;
   }

   @Override
   public TransportProviderDto getTransportProviderDto(MulticastDto dto) {
      return new TransportProviderDto().setComponentName(MULTICAST_TRANSPORT_PROVIDER_COMPONENT_NAME)
            .setConfigurationType(
                  dto.getBaseDto().getModelName() + getClassnameSuffix())
            .setProviderName(MULTICAST_PROVIDER_VARIABLE_NAME)
            .setTopic(MULTICAST_TOPIC)
            .setModule(MULTICAST_PROVIDER_MODULE);
   }

   @Override
   public Optional<MulticastDto> getConfigurationDto(GeneratedServiceConfigDto dto,
                                                     IJellyFishCommandOptions options,
                                                     IModel model, String topicsClassName,
                                                     Map<String, IDataReferenceField> topics) {

      MulticastDto multicastDto = new MulticastDto().setBaseDto(dto)
            .setTopicsImport(topicsClassName)
            .setClassname(dto.getModelName() + getClassnameSuffix());
      String topicsPrefix = topicsClassName.substring(topicsClassName.lastIndexOf('.') + 1) + '.';

      for (Map.Entry<String, IDataReferenceField> entry : topics.entrySet()) {
         String topicName = entry.getKey();
         IDataReferenceField field = entry.getValue();
         boolean isOutput = model.getOutputs().contains(field);
         boolean shouldSend = isOutput ^ test;
         Collection<MulticastConfiguration> configurations = transportConfigService.getMulticastConfiguration(options,
                                                                                                              field);
         int count = 1;
         for (MulticastConfiguration configuration : configurations) {
            String bindAddress;
            if (shouldSend) {
               bindAddress = configuration.getSourceInterface().getName();
            } else {
               bindAddress = configuration.getTargetInterface().getName();
            }
            MulticastTopicDto topicDto = new MulticastTopicDto().setGroupAddress(configuration.getGroupAddress())
                  .setBindAddress(bindAddress)
                  .setPort(configuration.getPort())
                  .setName(topicsPrefix + topicName)
                  .setVariableName(field.getName()
                                   + (configurations.size() > 1 ? count : ""))
                  .setSend(shouldSend);
            multicastDto.addTopic(topicDto);
            count++;
         }
      }

      if (multicastDto.getTopics().isEmpty()) {
         return Optional.empty();
      }

      return Optional.of(multicastDto);
   }

   @Override
   public String getTemplate() {
      return MULTICAST_TEMPLATE;
   }

   @Override
   public Set<String> getDependencies(IJellyFishCommandOptions options, IModel model, boolean topic, boolean provider,
            boolean module) {
      Set<String> dependencies = new LinkedHashSet<>();
      if (topic || provider) {
         dependencies.add(MULTICAST_TOPIC_DEPENDENCY);
      }
      if (provider) {
         dependencies.add(MULTICAST_PROVIDER_DEPENDENCY);
      }
      if (module) {
         dependencies.add(MULTICAST_MODULE_DEPENDENCY);
      }
      return dependencies;
   }

   private String getClassnameSuffix() {
      return test ? MULTICAST_TEST_CONFIGURATION_CLASS_NAME_SUFFIX : MULTICAST_CONFIGURATION_CLASS_NAME_SUFFIX;
   }

}
