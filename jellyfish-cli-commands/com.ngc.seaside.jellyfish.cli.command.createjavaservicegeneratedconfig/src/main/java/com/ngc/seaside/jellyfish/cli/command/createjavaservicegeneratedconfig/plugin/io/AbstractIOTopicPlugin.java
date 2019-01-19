/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.io;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationContext;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationType;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.DefaultTransportTopicConfigurationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.ITransportTopicConfigurationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.ITransportTopicConfigurationDto.TransportTopicDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.ITransportTopicConfigurationPlugin;
import com.ngc.seaside.jellyfish.service.codegen.api.IJavaServiceGenerationService;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.EnumDto;
import com.ngc.seaside.jellyfish.service.config.api.ITransportConfigurationService;
import com.ngc.seaside.jellyfish.service.scenario.api.IMessagingFlow;
import com.ngc.seaside.jellyfish.service.scenario.api.IScenarioService;
import com.ngc.seaside.jellyfish.service.scenario.api.MessagingParadigm;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

public abstract class AbstractIOTopicPlugin<T> implements ITransportTopicConfigurationPlugin<T> {

   protected final ITransportConfigurationService service;
   protected final IJavaServiceGenerationService generateService;
   protected final IScenarioService scenarioService;

   /**
    * Constructor.
    * 
    * @param service transport configuration service
    * @param generateService java service generation service
    * @param scenarioService scenario service
    */
   public AbstractIOTopicPlugin(ITransportConfigurationService service, IJavaServiceGenerationService generateService,
                                IScenarioService scenarioService) {
      this.service = service;
      this.generateService = generateService;
      this.scenarioService = scenarioService;
   }

   @Override
   public Set<ITransportTopicConfigurationDto<T>> getTopicConfigurations(ConfigurationContext context) {

      ConfigurationType configurationType = context.getConfigurationType();

      if (configurationType != ConfigurationType.SERVICE && configurationType != ConfigurationType.TEST) {
         return Collections.emptySet();
      }

      boolean internal = configurationType == ConfigurationType.SERVICE;

      IJellyFishCommandOptions options = context.getOptions();
      IModel model = context.getModel();

      EnumDto transportTopicsClass = generateService.getTransportTopicsDescription(options, model);
      String topicsType = transportTopicsClass.getFullyQualifiedName();

      Map<String, IOTopicDto> topics = new LinkedHashMap<>();

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

      Set<ITransportTopicConfigurationDto<T>> topicConfigurations = new LinkedHashSet<>();
      for (Entry<String, IOTopicDto> entry : topics.entrySet()) {
         String topicValue = entry.getKey();
         IOTopicDto ioTopicDto = entry.getValue();
         IDataReferenceField field = ioTopicDto.getField();
         MessagingParadigm paradigm = ioTopicDto.getParadigm();
         TransportTopicDto topic = new TransportTopicDto(topicsType, topicValue);
         boolean isOutput = model.getOutputs().contains(field);
         boolean shouldSend = !(isOutput ^ internal);
         Collection<T> configurations = getConfigurations(context, field, shouldSend, paradigm);
         for (T configuration : configurations) {
            DefaultTransportTopicConfigurationDto<T> dto = new DefaultTransportTopicConfigurationDto<>(configuration);
            dto.addTransportTopic(topic);
            topicConfigurations.add(dto);
         }
      }

      return topicConfigurations;
   }

   /**
    * Returns the configuration dtos associated with the given data reference field.
    * 
    * @param context context
    * @param field data reference field
    * @param shouldSend whether the configuration intends the topic to send a message or receive one
    * @param paradigm the messaging paradigm the field is associated with
    * @return the configuration dtos associated with the given data reference field
    */
   protected abstract Collection<T> getConfigurations(ConfigurationContext context, IDataReferenceField field,
            boolean shouldSend, MessagingParadigm paradigm);

   /**
    * Gets the topic name for the given flow and field and adds the topic with the field to the given map.
    *
    * @throws IllegalStateException if the map already contains the given topic with a different field
    */
   private void addToTopicsMap(Map<String, IOTopicDto> map, IMessagingFlow flow, IDataReferenceField field) {
      String topicName = service.getTransportTopicName(flow, field);
      IOTopicDto next = new IOTopicDto(field, flow.getMessagingParadigm());
      IOTopicDto previous = map.put(topicName, next);
      if (previous != null && !Objects.equals(previous, next)) {
         if (!Objects.equals(previous.getField(), next.getField())) {
            throw new IllegalStateException(
                     String.format("Two data reference fields assigned to the same topic %s: %s and %s",
                              topicName,
                              field.getName(),
                              previous.getField().getName()));
         } else {
            throw new IllegalStateException(
                     String.format(
                              "Cannot generate topic %s configuration for field that is used in multiple messaging "
                                       + "paradigms: %s",
                              topicName, field.getName()));
         }
      }
   }

   private static class IOTopicDto {

      private IDataReferenceField field;
      private MessagingParadigm paradigm;

      public IOTopicDto(IDataReferenceField field, MessagingParadigm paradigm) {
         this.field = field;
         this.paradigm = paradigm;
      }

      public IDataReferenceField getField() {
         return field;
      }

      public MessagingParadigm getParadigm() {
         return paradigm;
      }

      @Override
      public boolean equals(Object o) {
         if (!(o instanceof IOTopicDto)) {
            return false;
         }
         IOTopicDto that = (IOTopicDto) o;
         return Objects.equals(this.field, that.field)
                  && Objects.equals(this.paradigm, that.paradigm);
      }

      @Override
      public int hashCode() {
         return Objects.hash(field, paradigm);
      }
   }

}
