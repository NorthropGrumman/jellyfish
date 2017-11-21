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
import com.ngc.seaside.jellyfish.service.scenario.correlation.api.ICorrelationDescription;
import com.ngc.seaside.jellyfish.service.scenario.correlation.api.ICorrelationExpression;
import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataPath;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
                                IScenarioService scenarioService,
                                IDataService dataService,
                                IDataFieldGenerationService dataFieldGenerationService) {
      this.projectService = projectService;
      this.packageService = packageService;
      this.generateService = generateService;
      this.scenarioService = scenarioService;
      this.dataService = dataService;
      this.dataFieldGenerationService = dataFieldGenerationService;
   }

   public BaseServiceDto newDto(IJellyFishCommandOptions options, IModel model) {
      BaseServiceDto dto = new BaseServiceDto();
      return null;
   }

   private void setReceiveMethods(BaseServiceDto dto, IJellyFishCommandOptions options, IModel model) {
	   List<ReceiveDto> receiveDtos = new ArrayList<>();
	   for (IDataReferenceField input : model.getInputs()) {
		   List<String> basicScenarios = new ArrayList<>();
		   ReceiveDto receive = new ReceiveDto();
		   TypeDto<?> inputField = dataService.getEventClass(options, input.getType());
		   receive.setEventType(inputField.getTypeName());
		   
		   receive.setTopic(inputField.getTypeName() + ".TOPIC");
		   
		   receive.setName("receive" + inputField.getTypeName());
		   
		   for (IScenario scenario : model.getScenarios()) {
			   Optional<IPublishSubscribeMessagingFlow> flowOptional = scenarioService.getPubSubMessagingFlow(options,
			            scenario);
			
			   if (!flowOptional.isPresent()) {
				   continue;
			   }
			   
			   IPublishSubscribeMessagingFlow flow = flowOptional.get();
			   if (!flow.getInputs().contains(input)) {
				   continue;
			   }
			   
			   // scenario has this input
			   if (flow.getCorrelationDescription().isPresent() && !flow.getCorrelationDescription().get().getCompletenessExpressions().isEmpty()) {
				   receive.setHasCorrelations(true);
			   }
			   
			   // check for basic scenario
			   if (flow.getInputs().size() == 1 && flow.getOutputs().size() <= 1) {
				   basicScenarios.add("do" + StringUtils.capitalize(scenario.getName()));
			   }
		   }
		   receive.setBasicScenarios(basicScenarios);
		   receiveDtos.add(receive);
	   }
	   dto.setReceiveMethods(receiveDtos);
   }

   private void setPublishMethods(BaseServiceDto dto, IJellyFishCommandOptions options, IModel model) {
	   
	   List<PublishDto> publishDtos = new ArrayList<>();
	   for (IDataReferenceField output : model.getOutputs()) {
		   PublishDto publish = new PublishDto();
		   TypeDto<?> outputField = dataService.getEventClass(options, output.getType());
		   publish.setType(outputField.getTypeName());
		   publish.setTopic(outputField.getTypeName() + ".TOPIC");
		   publish.setName("publish" + outputField.getTypeName());   
		   publishDtos.add(publish);
	   }
	   
	   dto.setPublishMethods(publishDtos);
   }

   private void setBasicPubSubMethods(BaseServiceDto dto, IJellyFishCommandOptions options, IModel model) {

   }

   private void setBasicSinkMethods(BaseServiceDto dto, IJellyFishCommandOptions options, IModel model) {

   }

   private void setCorrelationMethods(BaseServiceDto dto, IJellyFishCommandOptions options, IModel model) {
      List<CorrelationDto> dtos = new ArrayList<>();
      for (IScenario scenario : model.getScenarios()) {
         Optional<IPublishSubscribeMessagingFlow> flowOptional = scenarioService.getPubSubMessagingFlow(options,
            scenario);

         if (!flowOptional.isPresent()) {
            continue;
         }

         IPublishSubscribeMessagingFlow flow = flowOptional.get();

         // Ignore scenarios with multiple outputs
         // Ignore scenarios without correlations
         if (flow.getOutputs().size() != 1 || !flow.getCorrelationDescription().isPresent()) {
            continue;
         }

         // Ignore flows with inputs of the same type
         if (flow.getInputs().stream().map(input -> input.getType()).distinct().count() != flow.getInputs().size()) {
            continue;
         }

         ICorrelationDescription description = flow.getCorrelationDescription().get();

         // Ignore scenarios without input-input correlations
         if (description.getCompletenessExpressions().isEmpty()) {
            continue;
         }

         CorrelationDto correlation = new CorrelationDto();

         correlation.setName("do" + StringUtils.capitalize(scenario.getName()));
         IData output = flow.getOutputs().iterator().next().getType();
         TypeDto<?> outputField = dataService.getEventClass(options, output);
         correlation.setOutputType(outputField.getTypeName());
         dto.getAbstractClass().getImports().add(outputField.getFullyQualifiedName());

         correlation.setServiceName(scenario.getName());

         correlation.setInputLogFormat(
            IntStream.range(0, flow.getInputs().size())
                     .mapToObj(i -> "%s")
                     .collect(Collectors.joining(", ")));

         correlation.setPublishMethod("publish" + StringUtils.capitalize(outputField.getTypeName()));

         correlation.setInputs(flow.getInputs()
                                   .stream()
                                   .map(input -> dataService.getEventClass(options, input.getType()))
                                   .map(type -> {
                                      dto.getAbstractClass().getImports().add(type.getFullyQualifiedName());
                                      return new InputDto().setType(type.getTypeName())
                                                           .setCorrelationMethod(correlation.getName());
                                   })
                                   .collect(Collectors.toList()));

         correlation.setInputOutputCorrelations(description.getCompletenessExpressions()
                                                           .stream()
                                                           .map(expression -> {
                                                              IDataPath left = expression.getLeftHandOperand();
                                                              IDataPath right = expression.getRightHandOperand();
                                                              IOCorrelationDto correlationDto = new IOCorrelationDto();
                                                              correlationDto.setGetterSnippet(
                                                                 left.getElements()
                                                                     .stream()
                                                                     .map(
                                                                        field -> dataFieldGenerationService.getEventsField(
                                                                           options, field).getJavaGetterName() + "()")
                                                                     .collect(Collectors.joining(".")));

                                                              correlationDto.setSetterSnippet(
                                                                 right.getElements()
                                                                      .subList(0, right.getElements().size() - 1)
                                                                      .stream()
                                                                      .map(
                                                                         field -> dataFieldGenerationService.getEventsField(
                                                                            options, field).getJavaGetterName() + "()")
                                                                      .collect(Collectors.joining("."))
                                                                    + "." + dataFieldGenerationService.getEventsField(
                                                                       options,
                                                                       expression.getRightHandOperand()
                                                                                 .getEnd()).getJavaSetterName());

                                                              correlationDto.setInputType(dataService.getEventClass(
                                                                 options, left.getStart().getType()).getTypeName());
                                                              return correlationDto;
                                                           })
                                                           .collect(Collectors.toList()));

         ICorrelationExpression completeness = description.getCompletenessExpressions()
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

         dtos.add(correlation);
      }
      dto.setCorrelationMethods(dtos);
   }

   private void setTriggerRegistrationMethods(BaseServiceDto dto, IJellyFishCommandOptions options, IModel model) {

   }

}
