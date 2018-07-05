package com.ngc.seaside.jellyfish.service.config.api.dto.telemetry;

public abstract class TelemetryReportingConfiguration {
   private int rateInMilliseconds;

   public int getRateInMilliseconds() {
      return rateInMilliseconds;
   }

   public TelemetryReportingConfiguration setRateInMilliseconds(int rateInMilliseconds) {
      this.rateInMilliseconds = rateInMilliseconds;
      return this;
   }

}
