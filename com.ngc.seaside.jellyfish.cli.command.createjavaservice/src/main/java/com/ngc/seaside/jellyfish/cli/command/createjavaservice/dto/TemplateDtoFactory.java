package com.ngc.seaside.jellyfish.cli.command.createjavaservice.dto;


import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.scenario.PublishStepHandler;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.scenario.ReceiveStepHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class TemplateDtoFactory implements ITemplateDtoFactory {

   @Override
   public TemplateDto newDto(IModel model, String packagez) {
      TemplateDto dto = createDto();
      setClassInfo(dto, model, packagez);
      setBaseClassInfo(dto, model, packagez);
      setInterfaceInfo(dto, model, packagez);
      setMethods(dto, model, packagez);
      setImports(dto, model, packagez);
      return dto;
   }

   protected TemplateDto createDto() {
      return new TemplateDto();
   }

   private static void setClassInfo(TemplateDto dto, IModel model, String packagez) {
      dto.setPackageName(packagez + ".impl")
            .setClassName(model.getName())
            .setArtifactId(model.getName().toLowerCase());
   }

   private static void setBaseClassInfo(TemplateDto dto, IModel model, String packagez) {
      dto.setBaseClassName("Abstract" + model.getName())
            .setBaseClassPackageName(packagez + ".base.impl");
   }

   private static void setInterfaceInfo(TemplateDto dto, IModel model, String packagez) {
      dto.setInterfaceName("I" + model.getName())
            .setInterfacePackageName(packagez + ".api");
   }

   private static void setMethods(TemplateDto dto, IModel model, String packagez) {
      List<MethodDto> methods = new ArrayList<>(model.getScenarios().size());
      for (IScenario scenario : model.getScenarios()) {
         methods.add(getMethod(scenario, packagez));
      }
      dto.setMethods(methods);
   }

   private static MethodDto getMethod(IScenario scenario, String packagez) {
      MethodDto dao;
      if (isReceivingAndPublishing(scenario)) {
         dao = getReceivingAndPublishingMethod(scenario, packagez);
      } else {
         // Should probably ignore this and not throw an exception.
         throw new IllegalArgumentException(String.format("scenario %s.%s contains no supported verbs!",
                                                          scenario.getParent().getFullyQualifiedName(),
                                                          scenario.getName()));
      }
      return dao;
   }

   private static boolean isReceivingAndPublishing(IScenario scenario) {
      boolean publishing = scenario.getThens()
            .stream()
            .anyMatch(s -> PublishStepHandler.FUTURE.getVerb().equals(s.getKeyword()));
      boolean receiving = scenario.getWhens()
            .stream()
            .anyMatch(s -> ReceiveStepHandler.PRESENT.getVerb().equals(s.getKeyword()));
      return publishing && receiving;
   }

   private static MethodDto getReceivingAndPublishingMethod(IScenario scenario, String packagez) {
      MethodDto dao = new MethodDto()
            .setOverride(true)
            .setMethodName(scenario.getName())
            .setReturns(true)
            .setArguments(new ArrayList<>());

      Collection<IScenarioStep> steps = scenario.getThens()
            .stream()
            .filter(s -> PublishStepHandler.FUTURE.getVerb().equals(s.getKeyword()))
            .collect(Collectors.toList());
      if (steps.size() > 1) {
         // TODO TH: create custom exception type that includes the model name and scenario name that makes debugging
         // easier.
         throw new IllegalArgumentException("only a single "
                                            + PublishStepHandler.FUTURE.getVerb()
                                            + " step per scenario is currently supported!");
      }
      IScenarioStep step = steps.iterator().next();
      dao.setReturnArgument(getEventArgument(scenario.getParent().getOutputs(),
                                             step.getParameters().get(0),
                                             packagez));

      scenario.getWhens()
            .stream()
            .filter(s -> ReceiveStepHandler.PRESENT.getVerb().equals(s.getKeyword()))
            .forEach(s -> dao.getArguments().add(getEventArgument(scenario.getParent().getInputs(),
                                                                  s.getParameters().get(0),
                                                                  packagez)));
      return dao;
   }

   private static ArgumentDto getEventArgument(INamedChildCollection<IModel, IDataReferenceField> fields,
                                               String fieldName,
                                               String packagez) {
      IDataReferenceField field = fields.getByName(fieldName).get();
      return new ArgumentDto()
            .setArgumentName(field.getName())
            .setArgumentClassName(field.getType().getName())
            .setArgumentPackageName(packagez + ".events");
   }

   private static void setImports(TemplateDto dao, IModel model, String packagez) {
      Set<String> imports = new TreeSet<>();
      imports.add(dao.getBaseClassPackageName() + "." + dao.getBaseClassName());
      imports.add(dao.getInterfacePackageName() + "." + dao.getInterfaceName());

      for (MethodDto m : dao.getMethods()) {
         if (m.isReturns()) {
            imports.add(m.getReturnArgument().getArgumentPackageName()
                        + "."
                        + m.getReturnArgument().getArgumentClassName());
         }

         for (ArgumentDto arg : m.getArguments()) {
            imports.add(arg.getArgumentPackageName()
                        + "."
                        + arg.getArgumentClassName());
         }
      }

      dao.setImports(imports);
   }
}
