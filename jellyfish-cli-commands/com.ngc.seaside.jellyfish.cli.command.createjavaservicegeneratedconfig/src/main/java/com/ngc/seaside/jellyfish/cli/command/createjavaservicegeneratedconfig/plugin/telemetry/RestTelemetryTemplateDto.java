package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.telemetry;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.BaseConfigurationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationContext;

public class RestTelemetryTemplateDto extends BaseConfigurationDto {

   private String className;

   public RestTelemetryTemplateDto(ConfigurationContext context, String className) {
      super(context);
      this.className = className;
   }

   public String getPackageName() {
      return getBasePackage();
   }

   public String getClassName() {
      return className;
   }

   public String getFullyQualifiedName() {
      return getPackageName() + '.' + getClassName();
   }

}
