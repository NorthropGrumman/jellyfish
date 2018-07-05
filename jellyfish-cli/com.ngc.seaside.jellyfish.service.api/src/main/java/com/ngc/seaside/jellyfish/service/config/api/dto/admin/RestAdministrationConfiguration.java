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
