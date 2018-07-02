package com.ngc.seaside.jellyfish.service.config.api.dto.telemetry;

public abstract class TelemetryReportingConfiguration {
   private int rateInSeconds;

   public int getRateInSeconds() {
      return rateInSeconds;
   }

   public TelemetryReportingConfiguration setRateInSeconds(int rateInSeconds) {
      this.rateInSeconds = rateInSeconds;
      return this;
   }

}
