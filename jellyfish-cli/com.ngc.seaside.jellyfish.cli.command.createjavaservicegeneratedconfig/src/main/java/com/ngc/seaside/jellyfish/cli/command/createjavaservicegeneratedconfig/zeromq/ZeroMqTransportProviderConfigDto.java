package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.zeromq;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.dto.GeneratedServiceConfigDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.dto.ITransportProviderConfigDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.dto.TransportProviderDto;
import com.ngc.seaside.jellyfish.service.config.api.ITransportConfigurationService;
import com.ngc.seaside.jellyfish.service.config.api.dto.zeromq.ZeroMqConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.zeromq.ZeroMqTcpTransportConfiguration;
import com.ngc.seaside.jellyfish.service.scenario.api.IPublishSubscribeMessagingFlow;
import com.ngc.seaside.jellyfish.service.scenario.api.IRequestResponseMessagingFlow;
import com.ngc.seaside.jellyfish.service.scenario.api.IScenarioService;
import com.ngc.seaside.jellyfish.service.scenario.api.MessagingParadigm;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public class ZeroMqTransportProviderConfigDto implements ITransportProviderConfigDto<ZeroMqDto> {
   static final String ZEROMQ_TRANSPORT_PROVIDER_COMPONENT_NAME = "com.ngc.seaside.service.transport.impl.provider.zeromq.ZeroMQTransportProvider";
   static final String ZEROMQ_CONFIGURATION_CLASS_NAME_SUFFIX = "ZeroMqConfiguration";
   static final String ZEROMQ_PROVIDER_VARIABLE_NAME = "zeroMqProvider";
   static final String ZEROMQ_TOPIC_PACKAGE_NAME = "com.ngc.seaside.service.transport.impl.topic.zeromq";
   static final String ZEROMQ_TOPIC_CLASS_NAME = "ZeroMQTopic";
   static final String ZEROMQ_TOPIC_DEPENDENCY = "com.ngc.seaside:service.transport.impl.topic.zeromq";
   static final String ZEROMQ_PROVIDER_DEPENDENCY = "com.ngc.seaside:service.transport.impl.provider.zeromq";
   public static final String ZEROMQ_TEMPLATE_SUFFIX = "zeromq";

   static final String CONNECTION_TYPE_BIND = "BIND";
   static final String CONNECTION_TYPE_CONNECT = "CONNECT";
   static final String SOCKET_TYPE_PUBLISH = "com.ngc.seaside.service.transport.impl.topic.zeromq.socket.PUB";
   static final String SOCKET_TYPE_SUBSCRIBE = "com.ngc.seaside.service.transport.impl.topic.zeromq.socket.SUB";
   static final String SOCKET_TYPE_REQUEST = "com.ngc.seaside.service.transport.impl.topic.zeromq.socket.REQ";
   static final String SOCKET_TYPE_RESPONSE = "com.ngc.seaside.service.transport.impl.topic.zeromq.socket.REP";

   private ITransportConfigurationService transportConfigService;
   private IScenarioService scenarioService;

   public ZeroMqTransportProviderConfigDto(ITransportConfigurationService transportConfigService,
                                           IScenarioService scenarioService) {
      this.transportConfigService = transportConfigService;
      this.scenarioService = scenarioService;
   }

   @Override
   public TransportProviderDto getTransportProviderDto(ZeroMqDto dto) {
      return new TransportProviderDto().setComponentName(ZEROMQ_TRANSPORT_PROVIDER_COMPONENT_NAME)
                                       .setConfigurationType(
                                          dto.getModelName() + ZEROMQ_CONFIGURATION_CLASS_NAME_SUFFIX)
                                       .setProviderName(ZEROMQ_PROVIDER_VARIABLE_NAME)
                                       .setTopicPackage(ZEROMQ_TOPIC_PACKAGE_NAME)
                                       .setTopicType(ZEROMQ_TOPIC_CLASS_NAME);
   }

   @Override
   public Optional<ZeroMqDto> getConfigurationDto(GeneratedServiceConfigDto dto,
            IJellyFishCommandOptions options,
            IModel model, String topicsClassName,
            Map<String, IDataReferenceField> topics) {

      ZeroMqDto zeroMqDto = new ZeroMqDto().setBaseDto(dto);
      zeroMqDto.addImport(topicsClassName);
      String topicsPrefix = topicsClassName.substring(topicsClassName.lastIndexOf('.') + 1) + '.';

      for (Map.Entry<String, IDataReferenceField> entry : topics.entrySet()) {
         String topicName = entry.getKey();
         IDataReferenceField field = entry.getValue();
         boolean isOutput = model.getOutputs().contains(field);

         Collection<ZeroMqConfiguration> configurations = transportConfigService.getZeroMqConfiguration(options,
            field);
         int count = 1;
         for (ZeroMqConfiguration configuration : configurations) {
            final Collection<ZeroMqTopicDto> zeroMqTopicDtos;

            if (configuration instanceof ZeroMqTcpTransportConfiguration) {
               zeroMqTopicDtos = getTcpConfigurationDtos(options,
                  topicsPrefix + topicName,
                  field.getName() + (configurations.size() > 1 ? count : ""),
                  field,
                  (ZeroMqTcpTransportConfiguration) configuration,
                  isOutput,
                  zeroMqDto::addImport);
            } else {
               throw new IllegalStateException("Zero MQ does not yet support non TCP configurations");
            }

            for (ZeroMqTopicDto topicDto : zeroMqTopicDtos) {
               zeroMqDto.addTopic(topicDto);
               count++;
            }
         }
      }

      if (zeroMqDto.getTopics().isEmpty()) {
         return Optional.empty();
      }

      return Optional.of(zeroMqDto);
   }

   private Collection<ZeroMqTopicDto> getTcpConfigurationDtos(IJellyFishCommandOptions options, String topicName,
            String variableName,
            IDataReferenceField field, ZeroMqTcpTransportConfiguration configuration, boolean isOutput,
            Consumer<String> imports) {

      Set<String> socketTypes = new LinkedHashSet<>();

      IModel model = field.getParent();
      for (IScenario scenario : model.getScenarios()) {
         Collection<MessagingParadigm> paradigms = scenarioService.getMessagingParadigms(options, scenario);
         for (MessagingParadigm paradigm : paradigms) {
            switch (paradigm) {
            case PUBLISH_SUBSCRIBE:
               IPublishSubscribeMessagingFlow pubSubFlow = scenarioService.getPubSubMessagingFlow(options, scenario)
                                                                          .get();
               if (isOutput) {
                  if (pubSubFlow.getOutputs().contains(field)) {
                     socketTypes.add(SOCKET_TYPE_PUBLISH);
                  }
               } else {
                  if (pubSubFlow.getInputs().contains(field)) {
                     socketTypes.add(SOCKET_TYPE_SUBSCRIBE);
                  }
               }
               break;
            case REQUEST_RESPONSE:
               IRequestResponseMessagingFlow reqRepFlow = scenarioService.getRequestResponseMessagingFlow(options,
                  scenario).get();
               if (isOutput) {
                  if (Objects.equals(reqRepFlow.getOutput(), field)) {
                     socketTypes.add(SOCKET_TYPE_RESPONSE);
                  }
               } else {
                  if (Objects.equals(reqRepFlow.getInput(), field)) {
                     socketTypes.add(SOCKET_TYPE_REQUEST);
                  }
               }
               break;
            default:
               throw new IllegalStateException("Unknown messaging paradigm: " + paradigm);
            }
         }
      }

      Collection<ZeroMqTopicDto> topicDtos = new ArrayList<>();
      for (String socketType : socketTypes) {
         ZeroMqTopicDto topicDto = new ZeroMqTopicDto();
         topicDto.setName(topicName);
         String socketTypeName = socketType.substring(socketType.lastIndexOf('.') + 1);
         topicDto.setVariableName(variableName + (socketTypes.size() > 1 ? socketTypeName : "") + "Topic");
         topicDto.setSocketType(socketTypeName);
         imports.accept(socketType);
         switch (configuration.getConnectionType()) {
         case SOURCE_BINDS_TARGET_CONNECTS:
            if (isOutput) {
               topicDto.setConnectionType(CONNECTION_TYPE_BIND);
               topicDto.setNetworkInterface(configuration.getBindConfiguration().getName());
            } else {
               topicDto.setConnectionType(CONNECTION_TYPE_CONNECT);
               topicDto.setNetworkInterface(configuration.getConnectConfiguration().getAddress());
            }
            break;
         case SOURCE_CONNECTS_TARGET_BINDS:
            if (isOutput) {
               topicDto.setConnectionType(CONNECTION_TYPE_CONNECT);
               topicDto.setNetworkInterface(configuration.getConnectConfiguration().getAddress());
            } else {
               topicDto.setConnectionType(CONNECTION_TYPE_BIND);
               topicDto.setNetworkInterface(configuration.getBindConfiguration().getName());
            }
            break;
         default:
            throw new IllegalStateException(
               "Unknown connection type: " + configuration.getConnectConfiguration());
         }
         topicDto.setPort(configuration.getPort());
         topicDto.setSend(isOutput);
         topicDtos.add(topicDto);
      }

      return topicDtos;
   }

   @Override
   public String getTemplateSuffix() {
      return ZEROMQ_TEMPLATE_SUFFIX;
   }

   @Override
   public Set<String> getDependencies(boolean distribution) {
      Set<String> dependencies = new LinkedHashSet<>();
      dependencies.add(ZEROMQ_TOPIC_DEPENDENCY);
      if (distribution) {
         dependencies.add(ZEROMQ_PROVIDER_DEPENDENCY);
      }
      return dependencies;
   }

}
