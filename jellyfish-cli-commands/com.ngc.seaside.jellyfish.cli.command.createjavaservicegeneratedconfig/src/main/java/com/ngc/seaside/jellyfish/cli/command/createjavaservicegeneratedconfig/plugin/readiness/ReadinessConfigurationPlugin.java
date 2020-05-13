/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.readiness;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.CreateJavaServiceGeneratedConfigCommand;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationContext;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationType;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.IConfigurationTemplatePlugin;
import com.ngc.seaside.jellyfish.service.codegen.api.IJavaServiceGenerationService;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.ClassDto;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

public class ReadinessConfigurationPlugin implements IConfigurationTemplatePlugin<ReadinessTemplateDto> {

   private Set<IReadinessPlugin> plugins;
   private IJavaServiceGenerationService genService;

   @Inject
   public ReadinessConfigurationPlugin(Set<IReadinessPlugin> plugins,
                                       IJavaServiceGenerationService genService) {
      this.plugins = plugins;
      this.genService = genService;
   }

   @Override
   public Optional<ReadinessTemplateDto> getConfigurationDto(ConfigurationContext context) {
      if (context.getConfigurationType() != ConfigurationType.SERVICE) {
         return Optional.empty();
      }

      ClassDto interfacez = genService.getServiceInterfaceDescription(context.getOptions(), context.getModel());
      ClassDto adviser = new ClassDto()
            .setPackageName(interfacez.getPackageName())
            .setTypeName(interfacez.getTypeName() + "Adviser");
      ReadinessTemplateDto dto = new ReadinessTemplateDto(context);
      dto.setAdviser(adviser);
      dto.getImports().add(adviser.getFullyQualifiedName());

      for (IReadinessPlugin plugin : plugins) {
         plugin.configure(dto);
      }

      if (dto.getComponents().isEmpty() && dto.getEventSubscribers().isEmpty() && dto.getClasses().isEmpty()) {
         return Optional.empty();
      }

      return Optional.of(dto);
   }

   @Override
   public String getTemplate(ConfigurationContext context) {
      return CreateJavaServiceGeneratedConfigCommand.class.getPackage().getName() + "-readiness";
   }

   @Override
   public Set<String> getDependencies(ConfigurationContext context, DependencyType dependencyType) {
      switch (dependencyType) {
         case COMPILE:
            return Collections.singleton("com.ngc.seaside:service.api");
         case BUNDLE:
            return Collections.singleton("com.ngc.seaside:service.readiness.impl.defaultreadinessservice");
         case MODULE:
            return Collections.singleton("com.ngc.seaside:service.readiness.impl.defaultreadinessservice.module");
         default:
            throw new AssertionError();
      }
   }
}
