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
package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportservice;

import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.CreateJavaServiceGeneratedConfigCommand;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationContext;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationType;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.IConfigurationTemplatePlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportprovider.ITransportProviderConfigurationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportprovider.ITransportProviderConfigurationPlugin;
import com.ngc.seaside.jellyfish.service.codegen.api.IJavaServiceGenerationService;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.ClassDto;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

/**
 * Plugin for configuring the transport service. This plugin expects {@link ITransportProviderConfigurationPlugin}s
 * and delegates most of its configuration to those plugins.
 */
public class TransportServiceConfigurationPlugin implements IConfigurationTemplatePlugin<TransportServiceTemplateDto> {

   private static final Set<String> COMPILE_DEPENDENCIES =
            setOf("com.ngc.seaside:service.transport.api", "com.ngc.blocs:service.api");
   private static final Set<String> BUNDLE_DEPENDENCIES =
            setOf("com.ngc.seaside:service.transport.impl.defaulttransportservice");
   private static final Set<String> MODULE_DEPENDENCIES =
            setOf("com.ngc.seaside:service.transport.impl.defaulttransportservice.module");

   private Set<ITransportProviderConfigurationPlugin<? extends ITransportProviderConfigurationDto>> plugins;
   private IJavaServiceGenerationService genService;

   @Inject
   public TransportServiceConfigurationPlugin(Set<
            ITransportProviderConfigurationPlugin<? extends ITransportProviderConfigurationDto>> plugins,
                                              IJavaServiceGenerationService genService) {
      this.plugins = plugins;
      this.genService = genService;
   }

   @Override
   public Optional<TransportServiceTemplateDto> getConfigurationDto(ConfigurationContext context) {
      ConfigurationType configurationType = context.getConfigurationType();
      if (configurationType != ConfigurationType.SERVICE && configurationType != ConfigurationType.TEST) {
         return Optional.empty();
      }

      TransportServiceTemplateDto dto = new TransportServiceTemplateDto(context);
      dto.setClassName(context.getModel().getName() + (configurationType == ConfigurationType.TEST ? "Test" : "")
               + "TransportConfiguration");
      dto.setSystem(isSystem(context));

      if (dto.isSystem()) {
         ClassDto interfacez = genService.getServiceInterfaceDescription(context.getOptions(), context.getModel());
         ClassDto adviser = new ClassDto()
               .setTypeName(interfacez.getTypeName() + "Adviser");
         dto.setAdviser(adviser);
         dto.getImports().add("com.ngc.seaside.service.transport.api.ITransportConfiguration");
      } else {
         ClassDto interfacez = genService.getServiceInterfaceDescription(context.getOptions(), context.getModel());
         ClassDto adviser = new ClassDto()
               .setPackageName(interfacez.getPackageName())
               .setTypeName(interfacez.getTypeName() + "Adviser");
         dto.setAdviser(adviser);
         dto.getImports().add(adviser.getFullyQualifiedName());
      }

      this.plugins.stream()
               .map(plugin -> plugin.getConfigurationDto(context))
               .filter(Optional::isPresent)
               .map(Optional::get)
               .forEach(dto::addTransportProvider);

      return Optional.of(dto);
   }

   @Override
   public String getTemplate(ConfigurationContext context) {
      if (context.getConfigurationType() == ConfigurationType.SERVICE) {
         return CreateJavaServiceGeneratedConfigCommand.class.getPackage().getName() + "-transportservice";
      } else {
         return CreateJavaServiceGeneratedConfigCommand.class.getPackage().getName() + "-transportserviceguice";
      }
   }

   @Override
   public Set<String> getDependencies(ConfigurationContext context, DependencyType dependencyType) {
      switch (dependencyType) {
         case COMPILE:
            return COMPILE_DEPENDENCIES;
         case BUNDLE:
            return BUNDLE_DEPENDENCIES;
         case MODULE:
            return MODULE_DEPENDENCIES;
         default:
            throw new AssertionError();
      }
   }

   @Override
   public Map<String, Object> getExtraTemplateParameters(ConfigurationContext context) {
      return Collections.singletonMap(StringUtils.class.getSimpleName(), StringUtils.class);
   }

   private static Set<String> setOf(String... values) {
      return Collections.unmodifiableSet(new LinkedHashSet<>(Arrays.asList(values)));
   }

   private static boolean isSystem(ConfigurationContext ctx) {
      return CommonParameters.evaluateBooleanParameter(ctx.getOptions().getParameters(),
                                                       CommonParameters.SYSTEM.getName(),
                                                       false);
   }
}
