package com.ngc.seaside.jellyfish.service.config.api.dto.telemetry;

import com.ngc.seaside.jellyfish.service.config.api.dto.RestConfiguration;

import java.util.Objects;

public class RestTelemetryReportingConfiguration extends TelemetryReportingConfiguration {
   private RestConfiguration config;

   public RestConfiguration getConfig() {
      return config;
   }

   public RestTelemetryReportingConfiguration setConfig(RestConfiguration config) {
      this.config = config;
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (!(o instanceof RestTelemetryReportingConfiguration)) {
         return false;
      }
      RestTelemetryReportingConfiguration that = (RestTelemetryReportingConfiguration) o;
      return this.getRateInSeconds() == that.getRateInSeconds()
               && Objects.equals(this.config, that.config);
   }

   @Override
   public int hashCode() {
      return Objects.hash(getRateInSeconds(), config);
   }

   @Override
   public String toString() {
      return "RestTelemetryReporting[rateInSeconds=" + getRateInSeconds() + "," + config.toString() + "]";
   }
}
