package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportservice;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.BaseConfigurationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationContext;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportprovider.ITransportProviderConfigurationDto;

import java.util.LinkedHashSet;
import java.util.Set;

public class TransportServiceTemplateDto extends BaseConfigurationDto {

   private final Set<ITransportProviderConfigurationDto> transportProviders = new LinkedHashSet<>();

   private String className;

   public TransportServiceTemplateDto(ConfigurationContext context) {
      super(context);
   }

   public String getClassName() {
      return className;
   }

   public TransportServiceTemplateDto setClassName(String className) {
      this.className = className;
      return this;
   }

   public String getPackageName() {
      return getBasePackage();
   }

   public Set<ITransportProviderConfigurationDto> getTransportProviders() {
      return transportProviders;
   }

   public TransportServiceTemplateDto addTransportProvider(ITransportProviderConfigurationDto transportProvider) {
      this.transportProviders.add(transportProvider);
      return this;
   }

}
