package com.ngc.seaside.jellyfish.service.config.api.dto.telemetry;

import com.ngc.seaside.jellyfish.service.config.api.dto.RestConfiguration;

import java.util.Objects;

public class RestTelemetryConfiguration extends TelemetryConfiguration {

   private RestConfiguration config;

   public RestConfiguration getConfig() {
      return config;
   }

   public RestTelemetryConfiguration setConfig(RestConfiguration config) {
      this.config = config;
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (!(o instanceof RestTelemetryConfiguration)) {
         return false;
      }
      RestTelemetryConfiguration that = (RestTelemetryConfiguration) o;
      return Objects.equals(this.config, that.config);
   }

   @Override
   public int hashCode() {
      return Objects.hash(config);
   }

   @Override
   public String toString() {
      return "RestTelemetry[" + config.toString() + "]";
   }

}
