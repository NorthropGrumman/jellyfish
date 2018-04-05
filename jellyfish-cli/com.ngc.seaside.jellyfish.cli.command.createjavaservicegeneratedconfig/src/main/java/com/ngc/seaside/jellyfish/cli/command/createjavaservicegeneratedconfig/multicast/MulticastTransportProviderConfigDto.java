package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.multicast;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
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
   public static final String MULTICAST_TEMPLATE_SUFFIX = "multicast";

   static final String MULTICAST_TRANSPORT_PROVIDER_COMPONENT_NAME = "com.ngc.seaside.service.transport.impl.provider.multicast.MulticastTransportProvider";
   static final String MULTICAST_CONFIGURATION_CLASS_NAME_SUFFIX = "MulticastConfiguration";
   static final String MULTICAST_PROVIDER_VARIABLE_NAME = "multicastProvider";
   static final String MULTICAST_TOPIC_PACKAGE_NAME = "com.ngc.seaside.service.transport.impl.topic.multicast";
   static final String MULTICAST_TOPIC_CLASS_NAME = "MulticastTopic";
   static final String MULTICAST_TOPIC_DEPENDENCY = "com.ngc.seaside:service.transport.impl.topic.multicast";
   static final String MULTICAST_PROVIDER_DEPENDENCY = "com.ngc.seaside:service.transport.impl.provider.multicast";


   private ITransportConfigurationService transportConfigService;

   public MulticastTransportProviderConfigDto(ITransportConfigurationService transportConfigService) {
      this.transportConfigService = transportConfigService;
   }

   @Override
   public TransportProviderDto getTransportProviderDto(MulticastDto dto) {
      return new TransportProviderDto().setComponentName(MULTICAST_TRANSPORT_PROVIDER_COMPONENT_NAME)
                                       .setConfigurationType(
                                          dto.getModelName() + MULTICAST_CONFIGURATION_CLASS_NAME_SUFFIX)
                                       .setProviderName(MULTICAST_PROVIDER_VARIABLE_NAME)
                                       .setTopicPackage(MULTICAST_TOPIC_PACKAGE_NAME)
                                       .setTopicType(MULTICAST_TOPIC_CLASS_NAME);
   }

   @Override
   public Optional<MulticastDto> getConfigurationDto(GeneratedServiceConfigDto dto,
            IJellyFishCommandOptions options,
            IModel model, String topicsClassName,
            Map<String, IDataReferenceField> topics) {

      MulticastDto multicastDto = new MulticastDto().setBaseDto(dto)
                                                    .setTopicsImport(topicsClassName);
      String topicsPrefix = topicsClassName.substring(topicsClassName.lastIndexOf('.') + 1) + '.';

      for (Map.Entry<String, IDataReferenceField> entry : topics.entrySet()) {
         String topicName = entry.getKey();
         IDataReferenceField field = entry.getValue();
         boolean isOutput = model.getOutputs().contains(field);

         Collection<MulticastConfiguration> configurations = transportConfigService.getMulticastConfiguration(options,
            field);
         int count = 1;
         for (MulticastConfiguration configuration : configurations) {
            MulticastTopicDto topicDto = new MulticastTopicDto().setGroupAddress(configuration.getGroupAddress())
                                                                .setSourceAddress(configuration.getSourceInterface().getName())
                                                                .setTargetAddress(configuration.getTargetInterface().getName())
                                                                .setPort(configuration.getPort())
                                                                .setName(topicsPrefix + topicName)
                                                                .setVariableName(field.getName()
                                                                   + (configurations.size() > 1 ? count : ""))
                                                                .setSend(isOutput);
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
   public String getTemplateSuffix() {
      return MULTICAST_TEMPLATE_SUFFIX;
   }

   @Override
   public Set<String> getDependencies(boolean distribution) {
      Set<String> dependencies = new LinkedHashSet<>();
      dependencies.add(MULTICAST_TOPIC_DEPENDENCY);
      if (distribution) {
         dependencies.add(MULTICAST_PROVIDER_DEPENDENCY);
      }
      return dependencies;
   }

}
