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
import com.ngc.seaside.jellyfish.service.scenario.correlation.api.ICorrelationDescription;
import com.ngc.seaside.jellyfish.service.scenario.correlation.api.ICorrelationExpression;
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
import java.util.Arrays;
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
