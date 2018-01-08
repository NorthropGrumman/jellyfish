package com.ngc.seaside.jellyfish.service.codegen.javaservice.impl;

import com.google.common.base.Preconditions;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.codegen.api.IJavaServiceGenerationService;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.ClassDto;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.EnumDto;
import com.ngc.seaside.jellyfish.service.config.api.ITransportConfigurationService;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.scenario.api.IPublishSubscribeMessagingFlow;
import com.ngc.seaside.jellyfish.service.scenario.api.IScenarioService;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

@Component(service = IJavaServiceGenerationService.class)
public class JavaServiceGenerationService implements IJavaServiceGenerationService {

   private IScenarioService scenarioService;
   private ITransportConfigurationService transportConfigService;
   private IPackageNamingService packageNamingService;
   private ILogService logService;

   @Override
   public ClassDto getServiceInterfaceDescription(IJellyFishCommandOptions options, IModel model) {
      Preconditions.checkNotNull(options, "options may not be null!");
      Preconditions.checkNotNull(model, "model may not be null!");

      ClassDto dto = new ClassDto();
      dto.setName("I" + model.getName());
      dto.setPackageName(packageNamingService.getServiceInterfacePackageName(options, model));
      dto.setImports(getImports(dto));

      return dto;
   }

   @Override
   public ClassDto getBaseServiceDescription(IJellyFishCommandOptions options, IModel model) {
      Preconditions.checkNotNull(options, "options may not be null!");
      Preconditions.checkNotNull(model, "model may not be null!");

      ClassDto dto = new ClassDto();
      dto.setName("Abstract" + model.getName());
      dto.setPackageName(packageNamingService.getServiceBaseImplementationPackageName(options, model));
      dto.setImports(getImports(dto));
      dto.getImports().add(packageNamingService.getServiceInterfacePackageName(options, model) + ".I" + model.getName());
      return dto;
   }

   @Override
   public EnumDto getTransportTopicsDescription(IJellyFishCommandOptions options, IModel model) {
      Set<String> transportTopics = new LinkedHashSet<>();
      for (IScenario scenario : model.getScenarios()) {
         Optional<IPublishSubscribeMessagingFlow> optionalFlow = scenarioService.getPubSubMessagingFlow(options, scenario);
         if (optionalFlow.isPresent()) {
            IPublishSubscribeMessagingFlow flow = optionalFlow.get();
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
      EnumDto dto = new EnumDto();
      dto.setValues(transportTopics)
         .setName(model.getName() + "TransportTopics")
         .setPackageName(packageNamingService.getTransportTopicsPackageName(options, model))
         .setImports(new LinkedHashSet<>(Collections.singleton("com.ngc.seaside.service.transport.api.ITransportTopic")));
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

   private static Set<String> getImports(ClassDto dto) {
      Set<String> imports = new TreeSet<>();
      return imports;
   }

}
