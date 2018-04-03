package com.ngc.seaside.jellyfish.service.config.api.dto;

import java.util.Objects;

/**
 * Defines the details for a {@link com.ngc.seaside.jellyfish.service.config.api.TransportConfigurationType#REST}
 * transport configuration.
 */
public class NetworkInterface {

   private String name;

   public NetworkInterface(String name){
      this.name = name;
   }

   public NetworkInterface(){}

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   @Override
   public boolean equals(Object o) {
      if (!(o instanceof NetworkInterface)) {
         return false;
      }
      NetworkInterface that = (NetworkInterface) o;
      return Objects.equals(this.name, that.name);
   }

   @Override
   public int hashCode() {
      return Objects.hash(name);
   }

   @Override
   public String toString() {
      return "RestConfiguration[name=" + name + "]";
   }

}
