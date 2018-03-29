package com.ngc.seaside.jellyfish.service.config.api.dto;

import java.util.Objects;

/**
 * Defines the details for a {@link com.ngc.seaside.jellyfish.service.config.api.TransportConfigurationType#MULTICAST}
 * transport configuration.
 */
public class MulticastConfiguration {

   private String address;
   private int port;

   public String getAddress() {
      return address;
   }

   public MulticastConfiguration setAddress(String address) {
      this.address = address;
      return this;
   }

   public int getPort() {
      return port;
   }

   public MulticastConfiguration setPort(int port) {
      this.port = port;
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (!(o instanceof MulticastConfiguration)) {
         return false;
      }
      MulticastConfiguration that = (MulticastConfiguration) o;
      return Objects.equals(this.address, that.address) && this.port == that.port;
   }

   @Override
   public int hashCode() {
      return Objects.hash(address, port);
   }

   @Override
   public String toString() {
      return "MulticastConfiguration[address=" + address + ", port=" + port + "]";
   }
}
