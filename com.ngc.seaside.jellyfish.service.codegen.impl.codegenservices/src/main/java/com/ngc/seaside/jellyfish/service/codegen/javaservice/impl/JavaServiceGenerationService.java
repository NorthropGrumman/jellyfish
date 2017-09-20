package com.ngc.seaside.jellyfish.service.codegen.javaservice.impl;

import com.google.common.base.Preconditions;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.codegen.api.IJavaServiceGenerationService;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.ArgumentDto;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.ClassDto;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.EnumDto;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.MethodDto;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.PubSubMethodDto;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.TypeDto;
import com.ngc.seaside.jellyfish.service.config.api.ITransportConfigurationService;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.scenario.api.IPublishSubscribeMessagingFlow;
import com.ngc.seaside.jellyfish.service.scenario.api.IScenarioService;
import com.ngc.seaside.jellyfish.service.scenario.api.MessagingParadigm;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Component(service = IJavaServiceGenerationService.class)
public class JavaServiceGenerationService implements IJavaServiceGenerationService {

   private IScenarioService scenarioService;
   private ITransportConfigurationService transportConfigService;
   private IPackageNamingService packageNamingService;
   private ILogService logService;

   @Override
   public ClassDto<MethodDto> getServiceInterfaceDescription(IJellyFishCommandOptions options, IModel model) {
      Preconditions.checkNotNull(options, "options may not be null!");
      Preconditions.checkNotNull(model, "model may not be null!");

      ClassDto<MethodDto> dto = new ClassDto<>();
      dto.setName("I" + model.getName());
      dto.setPackageName(packageNamingService.getServiceInterfacePackageName(options, model));
      List<MethodDto> methods = new ArrayList<>();
      getMethods(options, model, methods, new ArrayList<>());
      dto.setMethods(methods);
      dto.setImports(getImports(dto));

      return dto;
   }

   @Override
   public ClassDto<PubSubMethodDto> getBaseServiceDescription(IJellyFishCommandOptions options, IModel model) {
      Preconditions.checkNotNull(options, "options may not be null!");
      Preconditions.checkNotNull(model, "model may not be null!");

      ClassDto<PubSubMethodDto> dto = new ClassDto<>();
      dto.setName("Abstract" + model.getName());
      dto.setPackageName(packageNamingService.getServiceBaseImplementationPackageName(options, model));
      List<PubSubMethodDto> methods = new ArrayList<>();
      getMethods(options, model, new ArrayList<>(), methods);
      dto.setMethods(methods);
      dto.setImports(getImports(dto));
      dto.getImports().add(packageNamingService.getServiceInterfacePackageName(options, model) + ".I" + model.getName());
      return dto;
   }

   @Override
   public EnumDto<?> getTransportTopicsDescription(IJellyFishCommandOptions options, IModel model) {
      Set<String> transportTopics = new LinkedHashSet<>();
      for (IScenario scenario : model.getScenarios()) {
         for (IPublishSubscribeMessagingFlow flow : scenarioService.getPubSubMessagingFlows(options, scenario)) {
            for (IDataReferenceField field : flow.getInputs()) {
               String topic = transportConfigService.getTransportTopicName(flow, field);
               transportTopics.add(topic);
            }
            for (IDataReferenceField field : flow.getOutputs()) {
               String topic = transportConfigService.getTransportTopicName(flow, field);
               transportTopics.add(topic);
            }
         }
      }
      EnumDto<?> dto = new EnumDto<>();
      dto.setValues(transportTopics)
         .setName(model.getName() + "TransportTopics")
         .setPackageName(packageNamingService.getTransportTopicsPackageName(options, model))
         .setImplementedInterface(new ClassDto<>().setInterface(true).setName("ITransportTopic").setPackageName(
            "com.ngc.seaside.service.transport.api"))
         .setImports(Collections.singleton("com.ngc.seaside.service.transport.api.ITransportTopic"));
      return dto;
   }

   @Activate
   public void activate() {
      logService.debug(getClass(), "activated");
   }

   @Deactivate
   public void deactivate() {
      logService.debug(getClass(), "deactivated");
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

   public void removeLogService(ILogService ref) {
      setLogService(null);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
   public void setScenarioService(IScenarioService ref) {
      this.scenarioService = ref;
   }

   public void removeScenarioService(IScenarioService ref) {
      setScenarioService(null);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
   public void setPackageNamingService(IPackageNamingService ref) {
      this.packageNamingService = ref;
   }

   public void removePackageNamingService(IPackageNamingService ref) {
      setPackageNamingService(null);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
   public void setTransportConfigurationService(ITransportConfigurationService ref) {
      this.transportConfigService = ref;
   }

   public void removeTransportConfigurationService(ITransportConfigurationService ref) {
      setTransportConfigurationService(null);
   }

   /**
    * Gets methods for the service interface and base class and adds them to the given lists
    */
   private void getMethods(IJellyFishCommandOptions options, IModel model, List<MethodDto> interfaceMethods,
            List<PubSubMethodDto> baseClassMethods) {
      for (IScenario scenario : model.getScenarios()) {
         Collection<MessagingParadigm> paradigms = scenarioService.getMessagingParadigms(options, scenario);
         if (paradigms.contains(MessagingParadigm.PUBLISH_SUBSCRIBE)) {
            getPubSubMethods(options, scenario, interfaceMethods, baseClassMethods);
         } else if (paradigms.contains(MessagingParadigm.REQUEST_RESPONSE)) {
            interfaceMethods.addAll(getRequestResponseMethods(options, scenario));
         }
      }

      // Combine methods with the same signature
      Map<String, List<PubSubMethodDto>> aggregated = baseClassMethods.stream()
                                                                      .collect(Collectors.groupingBy(
                                                                         MethodDto::getMethodSignature));
      List<PubSubMethodDto> combinedMethods = new ArrayList<>(aggregated.size());
      for (Map.Entry<String, List<PubSubMethodDto>> entry : aggregated.entrySet()) {
         PubSubMethodDto first = entry.getValue().get(0);
         PubSubMethodDto method = new PubSubMethodDto();
         method.setName(first.getName());
         method.setReturns(first.isReturns());
         method.setReturnArgument(first.getReturnArgument());
         method.setArguments(first.getArguments());
         method.setPublishingTopic(first.getPublishingTopic());
         Map<String, MethodDto> publishers = new HashMap<>();
         for (PubSubMethodDto receiver : entry.getValue()) {
            if (receiver.getPublishMethods() != null) {
               publishers.putAll(receiver.getPublishMethods());
            }
         }
         method.setPublishMethods(publishers);
         combinedMethods.add(method);
      }
      baseClassMethods.clear();
      baseClassMethods.addAll(combinedMethods);
   }

   /**
    * Gets the pub/sub methods for the service interface and base class and adds them to the given lists
    */
   private void getPubSubMethods(IJellyFishCommandOptions options, IScenario scenario, List<MethodDto> interfaceMethods,
            List<PubSubMethodDto> baseClassMethods) {

      for (IPublishSubscribeMessagingFlow flow : scenarioService.getPubSubMessagingFlows(options, scenario)) {
         MethodDto[] method = null;

         switch (flow.getFlowType()) {
         case PATH:
            method = getMethodForPubSubFlowPath(options, flow);
            break;
         case SINK:
            method = getMethodForPubSubFlowSink(options, flow);
            break;
         case SOURCE:
            method = getMethodForPubSubFlowSource(options, flow);
            break;
         default:
            // TODO TH: implement other flow types.
            logService.warn(
               getClass(),
               "A pub/sub flow type of %s is not yet supported.  Encountered on scenario %s of model %s.",
               scenario.getName(),
               scenario.getParent().getFullyQualifiedName(),
               flow.getFlowType());
            break;
         }

         if (method != null && method.length > 0) {
            interfaceMethods.add(method[0]);
            for (int i = 1; i < method.length; i++) {
               baseClassMethods.add((PubSubMethodDto) method[i]);
            }
         }
      }

   }

   private List<MethodDto> getRequestResponseMethods(IJellyFishCommandOptions options, IScenario scenario) {
      // TODO TH: implement this
      return Collections.emptyList();
   }

   /**
    * Returns an array of methods: the interface method, the base publisher method, and the base subscriber method
    */
   private MethodDto[] getMethodForPubSubFlowPath(IJellyFishCommandOptions options,
            IPublishSubscribeMessagingFlow flow) {
      // TODO TH: handle data aggregation as part of a future story.
      if (flow.getInputs().size() > 1 || flow.getOutputs().size() > 1) {
         logService.warn(
            getClass(),
            "Pub/sub flow paths with of multiple inputs or outputs are not currently supported.  Encountered on"
               + " scenario %s of model %s.",
            flow.getScenario().getName(),
            flow.getScenario().getParent().getFullyQualifiedName());
         return null;
      }

      IDataReferenceField input = flow.getInputs().iterator().next();
      IDataReferenceField output = flow.getOutputs().iterator().next();

      MethodDto interfaceMethod = new MethodDto().setName(flow.getScenario().getName())
                                                 .setOverride(false)
                                                 .setReturns(true)
                                                 .setReturnArgument(
                                                    new ArgumentDto().setName(output.getName())
                                                                     .setTypeName(output.getType().getName())
                                                                     .setPackageName(
                                                                        packageNamingService.getEventPackageName(
                                                                           options,
                                                                           output.getType())))
                                                 .setArguments(
                                                    Collections.singletonList(
                                                       new ArgumentDto().setName(
                                                          input.getName())
                                                                        .setTypeName(input.getType().getName())
                                                                        .setPackageName(
                                                                           packageNamingService.getEventPackageName(
                                                                              options,
                                                                              input.getType()))));

      MethodDto publisherMethod = new PubSubMethodDto().setPublishingTopic(output.getType().getName() + ".TOPIC")
                                                       .setName("publish" + output.getType().getName())
                                                       .setOverride(false)
                                                       .setReturns(false)
                                                       .setArguments(Collections.singletonList(
                                                          new ArgumentDto().setName(output.getName())
                                                                           .setTypeName(
                                                                              output.getType().getName())
                                                                           .setPackageName(
                                                                              packageNamingService.getEventPackageName(
                                                                                 options,
                                                                                 output.getType()))));

      MethodDto subscriberMethod = new PubSubMethodDto().setPublishMethods(
                                                           Collections.singletonMap(flow.getScenario().getName(),
                                                              publisherMethod))
                                                        .setName("receive" + input.getType().getName())
                                                        .setOverride(false)
                                                        .setReturns(false)
                                                        .setArguments(Collections.singletonList(
                                                           new ArgumentDto().setName("event")
                                                                            .setTypeName("IEvent")
                                                                            .setPackageName(
                                                                               "com.ngc.blocs.service.event.api")
                                                                            .setTypes(Collections.singletonList(
                                                                               new ClassDto<>().setTypeName(
                                                                                  input.getType().getName())
                                                                                               .setPackageName(
                                                                                                  packageNamingService.getEventPackageName(
                                                                                                     options,
                                                                                                     input.getType()))))));

      return new MethodDto[] { interfaceMethod, publisherMethod, subscriberMethod };
   }

   /**
    * Returns an array of methods: the interface method, the base subscriber method
    */
   private MethodDto[] getMethodForPubSubFlowSink(IJellyFishCommandOptions options,
            IPublishSubscribeMessagingFlow flow) {
      // TODO TH: handle data aggregation as part of a future story.
      if (flow.getInputs().size() > 1) {
         logService.warn(
            getClass(),
            "Pub/sub flow sinks with of multiple inputs are not currently supported.  Encountered on"
               + " scenario %s of model %s.",
            flow.getScenario().getName(),
            flow.getScenario().getParent().getFullyQualifiedName());
         return null;
      }

      IDataReferenceField input = flow.getInputs().iterator().next();

      MethodDto interfaceMethod = new MethodDto()
                                                 .setName(flow.getScenario().getName())
                                                 .setOverride(false)
                                                 .setReturns(false)
                                                 .setArguments(Collections.singletonList(
                                                    new ArgumentDto()
                                                                     .setName(input.getName())
                                                                     .setTypeName(input.getType().getName())
                                                                     .setPackageName(
                                                                        packageNamingService.getEventPackageName(
                                                                           options,
                                                                           input.getType()))));

      MethodDto subscriberMethod = new PubSubMethodDto().setPublishMethods(
                                                           Collections.singletonMap(flow.getScenario().getName(), null))
                                                        .setName("receive" + input.getType().getName())
                                                        .setOverride(false)
                                                        .setReturns(false)
                                                        .setArguments(Collections.singletonList(
                                                           new ArgumentDto().setName("event")
                                                                            .setTypeName("IEvent")
                                                                            .setPackageName(
                                                                               "com.ngc.blocs.service.event.api")
                                                                            .setTypes(Collections.singletonList(
                                                                               new ClassDto<>().setTypeName(
                                                                                  input.getType().getName())
                                                                                               .setPackageName(
                                                                                                  packageNamingService.getEventPackageName(
                                                                                                     options,
                                                                                                     input.getType()))))));

      return new MethodDto[] { interfaceMethod, subscriberMethod };
   }

   /**
    * Returns an array of methods: the interface method, the base publisher method
    */
   private MethodDto[] getMethodForPubSubFlowSource(IJellyFishCommandOptions options,
            IPublishSubscribeMessagingFlow flow) {
      // TODO TH: handle data aggregation as part of a future story.
      if (flow.getOutputs().size() > 1) {
         logService.warn(
            getClass(),
            "Pub/sub flow sources with of multiple outputs are not currently supported.  Encountered on"
               + " scenario %s of model %s.",
            flow.getScenario().getName(),
            flow.getScenario().getParent().getFullyQualifiedName());
         return null;
      }

      IDataReferenceField output = flow.getOutputs().iterator().next();

      MethodDto interfaceMethod = new MethodDto()
                                                 .setName(flow.getScenario().getName())
                                                 .setOverride(false)
                                                 .setReturns(false)
                                                 .setArguments(Collections.singletonList(
                                                    new ArgumentDto()
                                                                     .setName("consumer")
                                                                     .setTypeName("Consumer")
                                                                     .setPackageName("java.util.function")
                                                                     .setTypes(Collections.singletonList(
                                                                        new ArgumentDto().setTypeName(
                                                                           output.getType().getName())
                                                                                         .setPackageName(
                                                                                            packageNamingService.getEventPackageName(
                                                                                               options,
                                                                                               output.getType()))))));

      MethodDto publisherMethod = new PubSubMethodDto().setPublishMethods(
                                                          Collections.singletonMap(flow.getScenario().getName(),
                                                             null))
                                                       .setPublishingTopic(output.getType().getName() + ".TOPIC")
                                                       .setName("publish" + output.getType().getName())
                                                       .setOverride(false)
                                                       .setReturns(false)
                                                       .setArguments(Collections.singletonList(
                                                          new ArgumentDto().setName(output.getName())
                                                                           .setTypeName(
                                                                              output.getType().getName())
                                                                           .setPackageName(
                                                                              packageNamingService.getEventPackageName(
                                                                                 options,
                                                                                 output.getType()))));

      return new MethodDto[] { interfaceMethod, publisherMethod };
   }

   private static Set<String> getImports(ClassDto<? extends MethodDto> dto) {
      Set<String> imports = new TreeSet<>();
      if (dto.getBaseClass() != null) {
         imports.add(dto.getBaseClass().getFullyQualifiedName());
      }
      if (dto.getImplementedInterface() != null) {
         imports.add(dto.getImplementedInterface().getFullyQualifiedName());
      }
      for (MethodDto method : dto.getMethods()) {
         if (method.isReturns()) {
            imports.add(method.getReturnArgument().getPackageName() + "." + method.getReturnArgument().getTypeName());
         }
         for (ArgumentDto arg : method.getArguments()) {
            imports.add(arg.getPackageName() + "." + arg.getTypeName());
            for (TypeDto<?> type : arg.getTypes()) {
               imports.add(type.getPackageName() + "." + type.getTypeName());
            }
         }
      }
      return imports;
   }

}
