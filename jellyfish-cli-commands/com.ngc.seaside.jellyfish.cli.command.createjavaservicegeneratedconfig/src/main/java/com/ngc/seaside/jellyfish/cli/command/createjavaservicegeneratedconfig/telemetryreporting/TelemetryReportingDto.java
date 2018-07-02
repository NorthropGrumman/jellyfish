package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.telemetryreporting;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.dto.GeneratedServiceConfigDto;
import com.ngc.seaside.jellyfish.service.config.api.dto.telemetry.TelemetryReportingConfiguration;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.LinkedHashSet;
import java.util.Set;

public class TelemetryReportingDto {

   public static final String ITELEMETRY_SERVICE = "com.ngc.seaside.service.telemetry.api.ITelemetryService";

   private GeneratedServiceConfigDto baseDto;
   private String classname;
   private int rateInMilliseconds;
   private String topicsImport;
   private String topic;
   private Set<TelemetryReportingConfiguration> configs = new LinkedHashSet<>();

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

   public TelemetryReportingDto setClassname(String classname) {
      this.classname = classname;
      return this;
   }

   public int getRateInMilliseconds() {
      return rateInMilliseconds;
   }

   public String getTopicsImport() {
      return topicsImport;
   }

   public TelemetryReportingDto setTopicsImport(String topicsImport) {
      this.topicsImport = topicsImport;
      return this;
   }

   public String getTopic() {
      return topic;
   }

   public TelemetryReportingDto setTopic(String topic) {
      this.topic = topic;
      return this;
   }

   public TelemetryReportingDto setRateInMilliseconds(int rate) {
      this.rateInMilliseconds = rate;
      return this;
   }

   public TelemetryReportingDto setBaseDto(GeneratedServiceConfigDto baseDto) {
      this.baseDto = baseDto;
      return this;
   }

   public Set<TelemetryReportingConfiguration> getConfigs() {
      return configs;
   }

   public TelemetryReportingDto addConfig(TelemetryReportingConfiguration config) {
      configs.add(config);
      return this;
   }

   /**
    * Adds the telemetry service and telemetry configuration classes to the readiness configuration.
    * 
    * @return this
    */
   public TelemetryReportingDto addReadinessConfigurations() {
      this.baseDto.addRequiredReadinessClasses(ITELEMETRY_SERVICE);
      this.baseDto.addRequiredReadinessClasses(getPackageName() + "." + getClassname());
      return this;
   }

}
