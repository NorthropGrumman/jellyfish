package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.zeromq;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.CreateJavaServiceGeneratedConfigCommand;
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
   static final String ZEROMQ_TEST_CONFIGURATION_CLASS_NAME_SUFFIX = "ZeroMqTestConfiguration";
   static final String ZEROMQ_PROVIDER_VARIABLE_NAME = "zeroMqProvider";
   static final String ZEROMQ_TOPIC = "com.ngc.seaside.service.transport.impl.topic.zeromq.ZeroMQTopic";
   static final String ZEROMQ_MODULE = "com.ngc.seaside.service.transport.impl.provider.zeromq.module.ZeroMQTransportProviderModule";
   static final String ZEROMQ_TOPIC_DEPENDENCY = "com.ngc.seaside:service.transport.impl.topic.zeromq";
   static final String ZEROMQ_PROVIDER_DEPENDENCY = "com.ngc.seaside:service.transport.impl.provider.zeromq";
   static final String ZEROMQ_MODULE_DEPENDENCY = "com.ngc.seaside:service.transport.impl.provider.zeromq.module";
   public static final String ZEROMQ_TEMPLATE = CreateJavaServiceGeneratedConfigCommand.class.getPackage().getName() + "-zeromq";

   static final String CONNECTION_TYPE_BIND = "BIND";
   static final String CONNECTION_TYPE_CONNECT = "CONNECT";
   static final String SOCKET_TYPE_PUBLISH = "com.ngc.seaside.service.transport.impl.topic.zeromq.socket.PUB";
   static final String SOCKET_TYPE_SUBSCRIBE = "com.ngc.seaside.service.transport.impl.topic.zeromq.socket.SUB";
   static final String SOCKET_TYPE_REQUEST = "com.ngc.seaside.service.transport.impl.topic.zeromq.socket.REQ";
   static final String SOCKET_TYPE_RESPONSE = "com.ngc.seaside.service.transport.impl.topic.zeromq.socket.REP";

   private final ITransportConfigurationService transportConfigService;
   private final IScenarioService scenarioService;
   private final boolean test;

   public ZeroMqTransportProviderConfigDto(ITransportConfigurationService transportConfigService,
                                           IScenarioService scenarioService,
                                           boolean test) {
      this.transportConfigService = transportConfigService;
      this.scenarioService = scenarioService;
      this.test = test;
   }

   @Override
   public TransportProviderDto getTransportProviderDto(ZeroMqDto dto) {
      return new TransportProviderDto().setComponentName(ZEROMQ_TRANSPORT_PROVIDER_COMPONENT_NAME)
                                       .setConfigurationType(
                                          dto.getBaseDto().getModelName() + getClassnameSuffix())
                                       .setProviderName(ZEROMQ_PROVIDER_VARIABLE_NAME)
                                       .setTopic(ZEROMQ_TOPIC)
                                       .setModule(ZEROMQ_MODULE);
   }

   @Override
   public Optional<ZeroMqDto> getConfigurationDto(GeneratedServiceConfigDto dto,
            IJellyFishCommandOptions options,
            IModel model, String topicsClassName,
            Map<String, IDataReferenceField> topics) {

      ZeroMqDto zeroMqDto = new ZeroMqDto()
               .setBaseDto(dto)
               .setClassname(dto.getModelName() + getClassnameSuffix());
      
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

   private String getClassnameSuffix() {
      return test ? ZEROMQ_TEST_CONFIGURATION_CLASS_NAME_SUFFIX : ZEROMQ_CONFIGURATION_CLASS_NAME_SUFFIX;
   }

   private Collection<ZeroMqTopicDto> getTcpConfigurationDtos(IJellyFishCommandOptions options, String topicName,
            String variableName,
            IDataReferenceField field, ZeroMqTcpTransportConfiguration configuration, boolean isOutput,
            Consumer<String> imports) {

      Set<String> socketTypes = new LinkedHashSet<>();

      final boolean shouldPublishOrRepsond = isOutput ^ test;
      IModel model = field.getParent();
      for (IScenario scenario : model.getScenarios()) {
         Collection<MessagingParadigm> paradigms = scenarioService.getMessagingParadigms(options, scenario);
         for (MessagingParadigm paradigm : paradigms) {
            switch (paradigm) {
            case PUBLISH_SUBSCRIBE:
               IPublishSubscribeMessagingFlow pubSubFlow = scenarioService.getPubSubMessagingFlow(options, scenario)
                                                                          .get();
               if ((isOutput && !pubSubFlow.getOutputs().contains(field)) ||
                  (!isOutput && !pubSubFlow.getInputs().contains(field))) {
                  break;
               }

               if (shouldPublishOrRepsond) {
                  socketTypes.add(SOCKET_TYPE_PUBLISH);
               } else {
                  socketTypes.add(SOCKET_TYPE_SUBSCRIBE);
               }
               break;
            case REQUEST_RESPONSE:
               IRequestResponseMessagingFlow reqRepFlow = scenarioService.getRequestResponseMessagingFlow(options,
                  scenario).get();

               if ((isOutput && !Objects.equals(reqRepFlow.getOutput(), field)) ||
                  (!isOutput && !Objects.equals(reqRepFlow.getInput(), field))) {
                  break;
               }
               if (shouldPublishOrRepsond) {
                  socketTypes.add(SOCKET_TYPE_RESPONSE);
               } else {
                  socketTypes.add(SOCKET_TYPE_REQUEST);
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
         final boolean shouldBind;
         switch (configuration.getConnectionType()) {
         case SOURCE_BINDS_TARGET_CONNECTS:
            shouldBind = isOutput ^ test;
            break;
         case SOURCE_CONNECTS_TARGET_BINDS:
            shouldBind = !(isOutput ^ test);
            break;
         default:
            throw new IllegalStateException(
               "Unknown connection type: " + configuration.getConnectConfiguration());
         }
         if (shouldBind) {
            topicDto.setConnectionType(CONNECTION_TYPE_BIND);
            topicDto.setNetworkInterface(configuration.getBindConfiguration().getName());
         } else {
            topicDto.setConnectionType(CONNECTION_TYPE_CONNECT);
            topicDto.setNetworkInterface(configuration.getConnectConfiguration().getAddress());
         }
         topicDto.setPort(configuration.getPort());
         topicDto.setSend(isOutput);
         topicDtos.add(topicDto);
      }

      return topicDtos;
   }

   @Override
   public String getTemplate() {
      return ZEROMQ_TEMPLATE;
   }

   @Override
   public Set<String> getDependencies(boolean topic, boolean provider, boolean module) {
      Set<String> dependencies = new LinkedHashSet<>();
      if (topic || provider) {
         dependencies.add(ZEROMQ_TOPIC_DEPENDENCY);
      }
      if (provider) {
         dependencies.add(ZEROMQ_PROVIDER_DEPENDENCY);
      }
      if (module) {
         dependencies.add(ZEROMQ_MODULE_DEPENDENCY);
      }
      return dependencies;
   }

}
