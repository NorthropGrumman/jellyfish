package com.ngc.seaside.jellyfish.cli.command.createjavacucumbertestsconfig.plugin;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.BaseConfigurationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationContext;

import java.util.LinkedHashSet;
import java.util.Set;

public class TransportServiceModuleTemplateDto extends BaseConfigurationDto {

   private final Set<String> modules = new LinkedHashSet<>();

   public TransportServiceModuleTemplateDto(ConfigurationContext context) {
      super(context);
   }

   public TransportServiceModuleTemplateDto addModule(String module) {
      this.modules.add(module);
      return this;
   }

   public String getPackageName() {
      return getBasePackage();
   }

   public Set<String> getModules() {
      return modules;
   }

}
