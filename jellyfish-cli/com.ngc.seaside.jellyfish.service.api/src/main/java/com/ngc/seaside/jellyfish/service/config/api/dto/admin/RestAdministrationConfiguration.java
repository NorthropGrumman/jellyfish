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
package com.ngc.seaside.jellyfish.service.config.api.dto.admin;

import com.ngc.seaside.jellyfish.service.config.api.dto.RestConfiguration;

import java.util.Objects;

public class RestAdministrationConfiguration extends AdministrationConfiguration {

   private RestConfiguration shutdown;
   private RestConfiguration restart;

   public RestConfiguration getShutdown() {
      return shutdown;
   }

   public RestAdministrationConfiguration setShutdown(RestConfiguration shutdown) {
      this.shutdown = shutdown;
      return this;
   }

   public RestConfiguration getRestart() {
      return restart;
   }

   public RestAdministrationConfiguration setRestart(RestConfiguration restart) {
      this.restart = restart;
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (!(o instanceof RestAdministrationConfiguration)) {
         return false;
      }
      RestAdministrationConfiguration that = (RestAdministrationConfiguration) o;
      return Objects.equals(this.shutdown, that.shutdown)
               && Objects.equals(this.restart, that.restart);
   }

   @Override
   public int hashCode() {
      return Objects.hash(shutdown, restart);
   }

   @Override
   public String toString() {
      return "RestAdministration[shutdown=" + shutdown + ",restart=" + restart + "]";
   }
}
