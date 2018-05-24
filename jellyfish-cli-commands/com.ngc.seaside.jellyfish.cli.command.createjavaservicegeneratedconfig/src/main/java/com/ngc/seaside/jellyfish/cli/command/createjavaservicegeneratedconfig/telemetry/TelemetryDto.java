package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.telemetry;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.dto.GeneratedServiceConfigDto;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

public class TelemetryDto {

   public static final String ITELEMETRY_SERVICE = "com.ngc.seaside.service.telemetry.api.ITelemetryService";

   private GeneratedServiceConfigDto baseDto;
   private String classname;

   public IModel getModel() {
      return baseDto.getModel();
   }

   public String getPackageName() {
      return baseDto.getPackageName();
   }

   public String getProjectDirectoryName() {
      return baseDto.getProjectDirectoryName();
   }

   public String getClassname() {
      return classname;
   }

   public TelemetryDto setClassname(String classname) {
      this.classname = classname;
      return this;
   }

   public TelemetryDto setBaseDto(GeneratedServiceConfigDto baseDto) {
      this.baseDto = baseDto;
      return this;
   }

   public TelemetryDto addReadinessConfigurations() {
      this.baseDto.addRequiredReadinessClasses(ITELEMETRY_SERVICE);
      this.baseDto.addRequiredReadinessClasses(getPackageName() + "." + getClassname());
      return this;
   }

}
