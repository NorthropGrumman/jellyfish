package com.ngc.seaside.jellyfish.service.codegen.javaservice.impl;

import com.google.common.base.Preconditions;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.codegen.api.IJavaServiceGenerationService;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.ArgumentDto;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.ClassDto;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.MethodDto;
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
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Component(service = IJavaServiceGenerationService.class)
public class JavaServiceGenerationService implements IJavaServiceGenerationService {

   private IScenarioService scenarioService;
   private IPackageNamingService packageNamingService;
   private ILogService logService;

   @Override
   public ClassDto getServiceInterfaceDescription(IJellyFishCommandOptions options, IModel model) {
      Preconditions.checkNotNull(options, "options may not be null!");
      Preconditions.checkNotNull(model, "model may not be null!");

      ClassDto dto = new ClassDto();
      dto.setName("I" + model.getName());
      dto.setPackageName(packageNamingService.getServiceInterfacePackageName(options, model));
      dto.setMethods(getMethods(options, model));
      dto.setImports(getImports(dto));

      return dto;
   }

   @Override
   public ClassDto getBaseServiceDescription(IJellyFishCommandOptions options, IModel model) {
      throw new UnsupportedOperationException("not implemented");
   }

   @Activate
   public void activate() {
      logService.debug(getClass(), "activated");
   }

   @Deactivate
   public void deactivate() {
      logService.debug(getClass(), "deactivated");
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC)
   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

   public void removeLogService(ILogService ref) {
      setLogService(null);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC)
   public void setScenarioService(IScenarioService ref) {
      this.scenarioService = ref;
   }

   public void removeScenarioService(IScenarioService ref) {
      setScenarioService(null);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC)
   public void setPackageNamingService(IPackageNamingService ref) {
      this.packageNamingService = ref;
   }

   public void removePackageNamingService(IPackageNamingService ref) {
      setPackageNamingService(null);
   }

   private List<MethodDto> getMethods(IJellyFishCommandOptions options, IModel model) {
      List<MethodDto> methods = new ArrayList<>();

      for (IScenario scenario : model.getScenarios()) {
         Collection<MessagingParadigm> paradigms = scenarioService.getMessagingParadigms(options, scenario);
         if (paradigms.contains(MessagingParadigm.PUBLISH_SUBSCRIBE)) {
            methods.addAll(getPubSubMethods(options, scenario));
         } else if (paradigms.contains(MessagingParadigm.REQUEST_RESPONSE)) {
            methods.addAll(getRequestResponseMethods(options, scenario));
         }
      }
      return methods;
   }

   private List<MethodDto> getPubSubMethods(IJellyFishCommandOptions options, IScenario scenario) {
      List<MethodDto> methods = new ArrayList<>();

      for (IPublishSubscribeMessagingFlow flow : scenarioService.getPubSubMessagingFlows(options, scenario)) {
         MethodDto method = null;

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

         if (method != null) {
            methods.add(method);
         }
      }

      return methods;
   }

   private List<MethodDto> getRequestResponseMethods(IJellyFishCommandOptions options, IScenario scenario) {
      // TODO TH: implement this
      return Collections.emptyList();
   }

   private MethodDto getMethodForPubSubFlowPath(IJellyFishCommandOptions options, IPublishSubscribeMessagingFlow flow) {
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

      return new MethodDto()
            .setName(flow.getScenario().getName())
            .setOverride(false)
            .setReturns(true)
            .setReturnArgument(
                  new ArgumentDto()
                        .setName(output.getName())
                        .setClassName(output.getType().getName())
                        .setPackageName(packageNamingService.getEventPackageName(options, output.getType())))
            .setArguments(Collections.singletonList(
                  new ArgumentDto()
                        .setName(input.getName())
                        .setClassName(input.getType().getName())
                        .setPackageName(packageNamingService.getEventPackageName(options, input.getType()))));
   }

   private MethodDto getMethodForPubSubFlowSink(IJellyFishCommandOptions options, IPublishSubscribeMessagingFlow flow) {
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

      return new MethodDto()
            .setName(flow.getScenario().getName())
            .setOverride(false)
            .setReturns(false)
            .setArguments(Collections.singletonList(
                  new ArgumentDto()
                        .setName(input.getName())
                        .setClassName(input.getType().getName())
                        .setPackageName(packageNamingService.getEventPackageName(options, input.getType()))));
   }

   private MethodDto getMethodForPubSubFlowSource(IJellyFishCommandOptions options,
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

      return new MethodDto()
            .setName(flow.getScenario().getName())
            .setOverride(false)
            .setReturns(true)
            .setReturnArgument(
                  new ArgumentDto()
                        .setName(output.getName())
                        .setClassName(output.getType().getName())
                        .setPackageName(packageNamingService.getEventPackageName(options, output.getType())))
            .setArguments(Collections.emptyList());
   }

   private static Set<String> getImports(ClassDto dto) {
      Set<String> imports = new TreeSet<>();
      for (MethodDto method : dto.getMethods()) {
         if (method.isReturns()) {
            imports.add(method.getReturnArgument().getPackageName());
         }
         for (ArgumentDto arg : method.getArguments()) {
            imports.add(arg.getPackageName());
         }
      }
      return imports;
   }
}
