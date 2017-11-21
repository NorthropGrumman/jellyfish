package com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto2;

import com.google.inject.Inject;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto2.CorrelationDto.IOCorrelationDto;
import com.ngc.seaside.jellyfish.service.codegen.api.IDataFieldGenerationService;
import com.ngc.seaside.jellyfish.service.codegen.api.IJavaServiceGenerationService;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.TypeDto;
import com.ngc.seaside.jellyfish.service.data.api.IDataService;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;
import com.ngc.seaside.jellyfish.service.scenario.api.IPublishSubscribeMessagingFlow;
import com.ngc.seaside.jellyfish.service.scenario.api.IScenarioService;
import com.ngc.seaside.jellyfish.service.scenario.correlation.api.ICorrelationExpression;
import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BaseServiceDtoFactory {

   private IProjectNamingService projectService;
   private IPackageNamingService packageService;
   private IJavaServiceGenerationService generateService;
   private IScenarioService scenarioService;
   private IDataService dataService;
   private IDataFieldGenerationService dataFieldGenerationService;

   @Inject
   public BaseServiceDtoFactory(IProjectNamingService projectService,
                                IPackageNamingService packageService,
                                IJavaServiceGenerationService generateService,
                                IScenarioService scenarioService) {
      this.projectService = projectService;
      this.packageService = packageService;
      this.generateService = generateService;
      this.scenarioService = scenarioService;
   }

   public BaseServiceDto newDto(IJellyFishCommandOptions options, IModel model) {
      BaseServiceDto dto = new BaseServiceDto();
      return null;
   }

   private void setReceiveMethods(BaseServiceDto dto, IJellyFishCommandOptions options, IModel model) {

   }

   private void setPublishMethods(BaseServiceDto dto, IJellyFishCommandOptions options, IModel model) {

   }

   private void setBasicPubSubMethods(BaseServiceDto dto, IJellyFishCommandOptions options, IModel model) {

   }

   private void setBasicSinkMethods(BaseServiceDto dto, IJellyFishCommandOptions options, IModel model) {

   }

   private void setCorrelationMethods(BaseServiceDto dto, IJellyFishCommandOptions options, IModel model) {
      List<CorrelationDto> dtos = new ArrayList<>();
      for (IScenario scenario : model.getScenarios()) {
         IPublishSubscribeMessagingFlow flow = scenarioService.getPubSubMessagingFlow(options, scenario);
         // Ignore scenarios without correlations
         if (!flow.getCorrelationDescription().isPresent()) {
            continue;
         }
         // Ignore scenarios without input-input correlations
         if (flow.getCorrelationDescription().get().getCompletenessExpressions().isEmpty()) {
            continue;
         }
         // Ignore scenarios with multiple outputs
         if (flow.getOutputs().size() != 1) {
            continue;
         }
         // Ignore correlations with inputs of the same type
         if (flow.getInputs().stream().map(input -> input.getType()).distinct().count() != flow.getInputs().size()) {
            continue;
         }
         CorrelationDto correlation = new CorrelationDto();
         correlation.setName("do" + scenario.getName().substring(0, 1).toUpperCase() + scenario.getName().substring(1));
         IData output = flow.getOutputs().iterator().next().getType();
         TypeDto<?> outputField = dataService.getEventClass(options, output);
         correlation.setOutputType(outputField.getTypeName());
         dto.getAbstractClass().getImports().add(outputField.getFullyQualifiedName());

         correlation.setServiceName(scenario.getName());

         correlation.setInputLogFormat(
            IntStream.range(0, flow.getInputs().size())
                     .mapToObj(i -> "%s")
                     .collect(Collectors.joining(", ")));

         correlation.setPublishMethod("publish" + outputField.getTypeName());

         correlation.setInputs(flow.getInputs()
                                   .stream()
                                   .map(input -> dataService.getEventClass(options, input.getType()))
                                   .map(type -> {
                                      dto.getAbstractClass().getImports().add(type.getFullyQualifiedName());
                                      return new InputDto().setType(type.getTypeName())
                                                           .setCorrelationMethod(correlation.getName());
                                   })
                                   .collect(Collectors.toList()));

         correlation.setInputOutputCorrelations(flow.getCorrelationDescription()
                                                    .get()
                                                    .getCompletenessExpressions()
                                                    .stream()
                                                    .map(expression -> {
                                                       IOCorrelationDto correlationDto = new IOCorrelationDto();
                                                       correlationDto.setGetterSnippet(expression.getLeftHandOperand()
                                                                                                 .getElements()
                                                                                                 .stream()
                                                                                                 .map(
                                                                                                    field -> dataFieldGenerationService.getEventsField(
                                                                                                       options, field)
                                                                                                                                       .getJavaGetterName()
                                                                                                       + "()")
                                                                                                 .collect(
                                                                                                    Collectors.joining(
                                                                                                       ".")));
                                                       correlationDto.setSetterSnippet(expression.getRightHandOperand()
                                                                                                 .getElements()
                                                                                                 .subList(0,
                                                                                                    expression.getRightHandOperand()
                                                                                                              .getElements()
                                                                                                              .size()
                                                                                                       - 1)
                                                                                                 .stream()
                                                                                                 .map(
                                                                                                    field -> dataFieldGenerationService.getEventsField(
                                                                                                       options, field)
                                                                                                                                       .getJavaGetterName()
                                                                                                       + "()")
                                                                                                 .collect(
                                                                                                    Collectors.joining(
                                                                                                       "."))
                                                          + "." + dataFieldGenerationService
                                                                                            .getEventsField(options,
                                                                                               expression.getRightHandOperand()
                                                                                                         .getEnd())
                                                                                            .getJavaSetterName());
                                                       return correlationDto;
                                                    })
                                                    .collect(Collectors.toList()));

         ICorrelationExpression completeness = flow.getCorrelationDescription()
                                                   .get()
                                                   .getCompletenessExpressions()
                                                   .iterator()
                                                   .next();
         switch (completeness.getCorrelationEventIdType()) {
         case DATA:
         case ENUM:
            INamedChild<IPackage> element = completeness.getCorrelationEventIdReferenceType();
            TypeDto<?> type = dataService.getEventClass(options, element);
            correlation.setCorrelationType(type.getTypeName());
            dto.getAbstractClass().getImports().add(type.getFullyQualifiedName());
            break;
         case BOOLEAN:
            correlation.setCorrelationType("Boolean.class");
            break;
         case FLOAT:
            correlation.setCorrelationType("Float.class");
            break;
         case INT:
            correlation.setCorrelationType("Integer.class");
            break;
         case STRING:
            correlation.setCorrelationType("String.class");
            break;
         }

      }
   }

   private void setTriggerRegistrationMethods(BaseServiceDto dto, IJellyFishCommandOptions options, IModel model) {

   }

}
