package com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto;

import com.ngc.seaside.jellyfish.cli.command.createjavaservice.dto.AbstractServiceDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservice.dto.ArgumentDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservice.dto.MethodDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservice.dto.TemplateDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservice.dto.TemplateDtoFactory;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class BaseServiceTemplateDaoFactory extends TemplateDtoFactory {

   @Override
   public TemplateDto newDto(IModel model, String packagez) {
      String basePackageName = String.format("%s.%s", model.getParent().getName(), model.getName().toLowerCase());
      BaseServiceTemplateDto dto = (BaseServiceTemplateDto) super.newDto(model, basePackageName);
      setBaseInfo(dto, model, basePackageName);
      setInterfaceImports(dto, model, packagez);
      setTransportTopics(dto, model, packagez);
      setPublishMethods(dto, model, packagez);
      setReceiveMethods(dto, model, packagez);
      setBaseClassInfoAndImports(dto, model, packagez);
      return dto;
   }

   @Override
   protected TemplateDto createDto() {
      return new BaseServiceTemplateDto();
   }

   private static void setBaseInfo(BaseServiceTemplateDto dto, IModel model, String basePackageName) {
      dto.setBasePackageName(basePackageName)
            .setExportedPackages(Collections.singleton(basePackageName + ".*"));
   }

   private static void setInterfaceImports(BaseServiceTemplateDto dto, IModel model, String packagez) {
      Set<String> imports = new TreeSet<>();
      for (MethodDto method : dto.getMethods()) {
         if (method.isReturns()) {
            imports.add(dto.getBasePackageName() + ".events." + method.getReturnArgument().getArgumentClassName());
         }
         for (ArgumentDto arg : method.getArguments()) {
            imports.add(dto.getBasePackageName() + ".events." + arg.getArgumentClassName());
         }
      }
      dto.getServiceInterfaceDto().setImports(imports);
   }

   private static void setTransportTopics(BaseServiceTemplateDto dto, IModel model, String packagez) {
      Set<String> topics = new TreeSet<>();
      for (MethodDto method : dto.getMethods()) {
         if (method.isReturns()) {
            topics.add(constantize(method.getReturnArgument().getArgumentClassName()));
         }
         for (ArgumentDto arg : method.getArguments()) {
            topics.add(constantize(arg.getArgumentClassName()));
         }
      }
      dto.setTransportTopics(topics);
   }

   private static void setPublishMethods(BaseServiceTemplateDto dto,
                                         IModel model,
                                         String packagez) {
      // TODO TH: this is a hacky way to figure out what to publish.
      // Use model output instead; however we need to have the scenario to understand *how* the service outputs the
      // output.
      List<MethodDto> methods = new ArrayList<>(dto.getMethods().size());
      for (MethodDto methodDto : dto.getMethods()) {
         if (methodDto.isReturns()) {
            MethodDto m = new MethodDto()
                  .setOverride(false)
                  .setReturns(false)
                  .setMethodName("publish" + methodDto.getReturnArgument().getArgumentClassName())
                  .setArguments(Collections.singletonList(methodDto.getReturnArgument()));
            methods.add(m);
         }
      }
      dto.setPublishingMethods(methods);
   }

   private static void setReceiveMethods(BaseServiceTemplateDto dto,
                                         IModel model,
                                         String packagez) {
      // TODO TH: this is a hacky way to figure out what to receive.
      List<MethodDto> methods = new ArrayList<>(dto.getMethods().size());
      for (MethodDto methodDto : dto.getMethods()) {
         if (methodDto.isReturns()) {
            // TODO TH: handle scenarios with multiple inputs.
            ArgumentDto argument = methodDto.getArguments().get(0);
            ArgumentDto eventArg = new ArgumentDto()
                  .setArgumentPackageName(argument.getArgumentPackageName())
                  .setArgumentName("event")
                  .setArgumentClassName(String.format("IEvent<%s>", argument.getArgumentClassName()));
            ReceiveMethodDto m = new ReceiveMethodDto()
                  .setEventSourceClassName(argument.getArgumentClassName())
                  .setInterfaceMethod(methodDto)
                  .setPublishMethod(
                        dto.getPublishingMethods()
                              .stream()
                              .filter(q -> q.getMethodName().equals("publish" + methodDto.getReturnArgument()
                                    .getArgumentClassName()))
                              .findAny()
                              .orElseThrow(() -> new IllegalStateException(
                                    "couldn't find method for "
                                    + methodDto.getReturnArgument().getArgumentClassName())));
            m.setOverride(false)
                  .setReturns(false)
                  .setMethodName("receive" + argument.getArgumentClassName())
                  .setArguments(Collections.singletonList(eventArg));
            methods.add(m);
         }
      }
      dto.setReceivingMethods(methods);
   }

   private static void setBaseClassInfoAndImports(BaseServiceTemplateDto dto, IModel model, String packagez) {
      Set<String> imports = new TreeSet<>();
      for (MethodDto receive : dto.getMethods()) {
         // TODO TH: handle scenarios with multiple inputs.
         ArgumentDto arg = receive.getArguments().get(0);
         imports.add(String.format("%s.%s", arg.getArgumentPackageName(), arg.getArgumentClassName()));
      }
      for (MethodDto publish : dto.getPublishingMethods()) {
         for (ArgumentDto arg : publish.getArguments()) {
            imports.add(String.format("%s.%s", arg.getArgumentPackageName(), arg.getArgumentClassName()));
         }
      }
      imports.add(String.format("%s.%s",
                                dto.getServiceInterfaceDto().getPackageName(),
                                dto.getServiceInterfaceDto().getInterfaceName()));

      dto.getAbstractServiceDto()
            .setModelName(model.getFullyQualifiedName())
            .setImports(imports);
   }
   
   private static String constantize(String value) {
      char[] chars = value.toCharArray();
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < chars.length; i++) {
         char c = chars[i];
         if (i > 0 && Character.isUpperCase(c)) {
            sb.append("_");
         }
         sb.append(Character.toUpperCase(c));
      }
      return sb.toString();
   }
}
