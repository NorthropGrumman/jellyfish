package com.ngc.seaside.jellyfish.service.config.api.dto;

import java.util.Objects;

/**
 * Defines the details for a {@link com.ngc.seaside.jellyfish.service.config.api.TransportConfigurationType#REST}
 * transport configuration.
 */
public class NetworkInterface {

   private String address;

   public String getAddress() {
      return address;
   }

   public void setAddress(String address) {
      this.address = address;
   }

   @Override
   public boolean equals(Object o) {
      if (!(o instanceof NetworkInterface)) {
         return false;
      }
      NetworkInterface that = (NetworkInterface) o;
      return Objects.equals(this.address, that.address);
   }

   @Override
   public int hashCode() {
      return Objects.hash(address);
   }

   @Override
   public String toString() {
      return "RestConfiguration[address=" + address + "]";
   }

}
