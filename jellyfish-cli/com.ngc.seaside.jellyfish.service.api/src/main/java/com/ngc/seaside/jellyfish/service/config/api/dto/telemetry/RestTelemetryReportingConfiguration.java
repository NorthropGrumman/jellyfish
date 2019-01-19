/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
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
      return this.getRateInMilliseconds() == that.getRateInMilliseconds()
               && Objects.equals(this.config, that.config);
   }

   @Override
   public int hashCode() {
      return Objects.hash(getRateInMilliseconds(), config);
   }

   @Override
   public String toString() {
      return "RestTelemetryReporting[rateInSeconds=" + getRateInMilliseconds() + "," + config.toString() + "]";
   }
}
